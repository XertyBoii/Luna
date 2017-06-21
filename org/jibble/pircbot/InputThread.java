package org.jibble.pircbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class InputThread
  extends Thread
{
  InputThread(PircBot bot, Socket socket, BufferedReader breader, BufferedWriter bwriter)
  {
    this._bot = bot;
    this._socket = socket;
    this._breader = breader;
    this._bwriter = bwriter;
    setName(getClass() + "-Thread");
  }
  
  void sendRawLine(String line)
  {
    OutputThread.sendRawLine(this._bot, this._bwriter, line);
  }
  
  boolean isConnected()
  {
    return this._isConnected;
  }
  
  public void run()
  {
    try
    {
      boolean running = true;
      while (running) {
        try
        {
          String line = null;
          while ((line = this._breader.readLine()) != null) {
            try
            {
              this._bot.handleLine(line);
            }
            catch (Throwable t)
            {
              StringWriter sw = new StringWriter();
              PrintWriter pw = new PrintWriter(sw);
              t.printStackTrace(pw);
              pw.flush();
              StringTokenizer tokenizer = new StringTokenizer(sw.toString(), "\r\n");
              synchronized (this._bot)
              {
                this._bot.log("### Your implementation of PircBot is faulty and you have");
                this._bot.log("### allowed an uncaught Exception or Error to propagate in your");
                this._bot.log("### code. It may be possible for PircBot to continue operating");
                this._bot.log("### normally. Here is the stack trace that was produced: -");
                this._bot.log("### ");
                while (tokenizer.hasMoreTokens()) {
                  this._bot.log("### " + tokenizer.nextToken());
                }
              }
            }
          }
          if (line == null) {
            running = false;
          }
        }
        catch (InterruptedIOException iioe)
        {
          sendRawLine("PING " + System.currentTimeMillis() / 1000L);
        }
      }
    }
    catch (Exception localException) {}
    try
    {
      this._socket.close();
    }
    catch (Exception localException1) {}
    if (!this._disposed)
    {
      this._bot.log("*** Disconnected.");
      this._isConnected = false;
      this._bot.onDisconnect();
    }
  }
  
  public void dispose()
  {
    try
    {
      this._disposed = true;
      this._socket.close();
    }
    catch (Exception localException) {}
  }
  
  private PircBot _bot = null;
  private Socket _socket = null;
  private BufferedReader _breader = null;
  private BufferedWriter _bwriter = null;
  private boolean _isConnected = true;
  private boolean _disposed = false;
  public static final int MAX_LINE_LENGTH = 512;
}
