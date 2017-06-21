package org.jibble.pircbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class IdentServer
  extends Thread
{
  private PircBot _bot;
  private String _login;
  
  IdentServer(PircBot bot, String login)
  {
    this._bot = bot;
    this._login = login;
    try
    {
      this._ss = new ServerSocket(113);
      this._ss.setSoTimeout(60000);
    }
    catch (Exception e)
    {
      this._bot.log("*** Could not start the ident server on port 113.");
      return;
    }
    this._bot.log("*** Ident server running on port 113 for the next 60 seconds...");
    setName(getClass() + "-Thread");
    start();
  }
  
  public void run()
  {
    try
    {
      Socket socket = this._ss.accept();
      socket.setSoTimeout(60000);
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      String line = reader.readLine();
      if (line != null)
      {
        this._bot.log("*** Ident request received: " + line);
        line = line + " : USERID : UNIX : " + this._login;
        writer.write(line + "\r\n");
        writer.flush();
        this._bot.log("*** Ident reply sent: " + line);
        writer.close();
      }
    }
    catch (Exception localException) {}
    try
    {
      this._ss.close();
    }
    catch (Exception localException1) {}
    this._bot.log("*** The Ident server has been shut down.");
  }
  
  private ServerSocket _ss = null;
}
