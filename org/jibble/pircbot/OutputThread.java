package org.jibble.pircbot;

import java.io.BufferedWriter;

public class OutputThread
  extends Thread
{
  OutputThread(PircBot bot, Queue outQueue)
  {
    this._bot = bot;
    this._outQueue = outQueue;
    setName(getClass() + "-Thread");
  }
  
  static void sendRawLine(PircBot bot, BufferedWriter bwriter, String line)
  {
    if (line.length() > bot.getMaxLineLength() - 2) {
      line = line.substring(0, bot.getMaxLineLength() - 2);
    }
    synchronized (bwriter)
    {
      try
      {
        bwriter.write(line + "\r\n");
        bwriter.flush();
        bot.log(">>>" + line);
      }
      catch (Exception localException) {}
    }
  }
  
  public void run()
  {
    try
    {
      boolean running = true;
      while (running)
      {
        Thread.sleep(this._bot.getMessageDelay());
        String line = (String)this._outQueue.next();
        if (line != null) {
          this._bot.sendRawLine(line);
        } else {
          running = false;
        }
      }
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  private PircBot _bot = null;
  private Queue _outQueue = null;
}
