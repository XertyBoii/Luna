package org.jibble.pircbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import lunadevs.luna.irc.IrcChatLine;
import lunadevs.luna.main.Luna;

public abstract class PircBot
  implements ReplyConstants
{
  public static final String VERSION = "1.5.0";
  private static final int OP_ADD = 1;
  private static final int OP_REMOVE = 2;
  private static final int VOICE_ADD = 3;
  private static final int VOICE_REMOVE = 4;
  
  public final synchronized void connect(String hostname)
    throws IOException, IrcException, NickAlreadyInUseException
  {
    connect(hostname, 6667, null);
  }
  
  public final synchronized void connect(String hostname, int port)
    throws IOException, IrcException, NickAlreadyInUseException
  {
    connect(hostname, port, null);
  }
  
  public final synchronized void connect(String hostname, int port, String password)
    throws IOException, IrcException, NickAlreadyInUseException
  {
    this._server = hostname;
    this._port = port;
    this._password = password;
    if (isConnected()) {
      throw new IOException("The PircBot is already connected to an IRC server.  Disconnect first.");
    }
    removeAllChannels();
    
    Socket socket = new Socket(hostname, port);
    log("*** Connected to server.");
    this._inetAddress = socket.getLocalAddress();
    InputStreamReader inputStreamReader = null;
    OutputStreamWriter outputStreamWriter = null;
    if (getEncoding() != null)
    {
      inputStreamReader = new InputStreamReader(socket.getInputStream(), getEncoding());
      outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), getEncoding());
    }
    else
    {
      inputStreamReader = new InputStreamReader(socket.getInputStream());
      outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
    }
    BufferedReader breader = new BufferedReader(inputStreamReader);
    BufferedWriter bwriter = new BufferedWriter(outputStreamWriter);
    if ((password != null) && (!password.equals(""))) {
      OutputThread.sendRawLine(this, bwriter, "PASS " + password);
    }
    String nick = getName();
    OutputThread.sendRawLine(this, bwriter, "NICK " + nick);
    OutputThread.sendRawLine(this, bwriter, "USER " + getLogin() + " 8 * :" + getVersion());
    this._inputThread = new InputThread(this, socket, breader, bwriter);
    
    String line = null;
    int tries = 1;
    while ((line = breader.readLine()) != null)
    {
      handleLine(line);
      int firstSpace = line.indexOf(" ");
      int secondSpace = line.indexOf(" ", firstSpace + 1);
      if (secondSpace >= 0)
      {
        String code = line.substring(firstSpace + 1, secondSpace);
        if (code.equals("004")) {
          break;
        }
        if (code.equals("433"))
        {
          if (this._autoNickChange)
          {
            tries++;
            nick = getName() + tries;
            OutputThread.sendRawLine(this, bwriter, "NICK " + nick);
          }
          else
          {
            socket.close();
            this._inputThread = null;
            throw new NickAlreadyInUseException(line);
          }
        }
        else if (!code.equals("439")) {
          if ((code.startsWith("5")) || (code.startsWith("4")))
          {
            socket.close();
            this._inputThread = null;
            throw new IrcException("Could not log into the IRC server: " + line);
          }
        }
      }
      setNick(nick);
    }
    log("*** Logged onto server.");
    
    socket.setSoTimeout(300000);
    
    this._inputThread.start();
    if (this._outputThread == null)
    {
      this._outputThread = new OutputThread(this, this._outQueue);
      this._outputThread.start();
    }
    onConnect();
  }
  
  public final synchronized void reconnect()
    throws IOException, IrcException, NickAlreadyInUseException
  {
    if (getServer() == null) {
      throw new IrcException("Cannot reconnect to an IRC server because we were never connected to one previously!");
    }
    connect(getServer(), getPort(), getPassword());
  }
  
  public final synchronized void disconnect()
  {
    quitServer();
  }
  
  public void setAutoNickChange(boolean autoNickChange)
  {
    this._autoNickChange = autoNickChange;
  }
  
  public final void startIdentServer()
  {
    new IdentServer(this, getLogin());
  }
  
  public final void joinChannel(String channel)
  {
    sendRawLine("JOIN " + channel);
  }
  
  public final void joinChannel(String channel, String key)
  {
    joinChannel(channel + " " + key);
  }
  
  public final void partChannel(String channel)
  {
    sendRawLine("PART " + channel);
  }
  
  public final void partChannel(String channel, String reason)
  {
    sendRawLine("PART " + channel + " :" + reason);
  }
  
  public final void quitServer()
  {
    quitServer("");
  }
  
  public final void quitServer(String reason)
  {
    sendRawLine("QUIT :" + reason);
  }
  
  public final synchronized void sendRawLine(String line)
  {
    if (isConnected()) {
      this._inputThread.sendRawLine(line);
    }
  }
  
  public final synchronized void sendRawLineViaQueue(String line)
  {
    if (line == null) {
      throw new NullPointerException("Cannot send null messages to server");
    }
    if (isConnected()) {
      this._outQueue.add(line);
    }
  }
  
  public final void sendMessage(String target, String message)
  {
    this._outQueue.add("PRIVMSG " + target + " :" + message);
  }
  
  public final void sendAction(String target, String action)
  {
    sendCTCPCommand(target, "ACTION " + action);
  }
  
  public final void sendNotice(String target, String notice)
  {
    this._outQueue.add("NOTICE " + target + " :" + notice);
  }
  
  public final void sendCTCPCommand(String target, String command)
  {
    this._outQueue.add("PRIVMSG " + target + " :\001" + command + "\001");
  }
  
  public final void changeNick(String newNick)
  {
    sendRawLine("NICK " + newNick);
  }
  
  public final void identify(String password)
  {
    sendRawLine("NICKSERV IDENTIFY " + password);
  }
  
  public final void setMode(String channel, String mode)
  {
    sendRawLine("MODE " + channel + " " + mode);
  }
  
  public final void sendInvite(String nick, String channel)
  {
    sendRawLine("INVITE " + nick + " :" + channel);
  }
  
  public final void ban(String channel, String hostmask)
  {
    sendRawLine("MODE " + channel + " +b " + hostmask);
  }
  
  public final void unBan(String channel, String hostmask)
  {
    sendRawLine("MODE " + channel + " -b " + hostmask);
  }
  
  public final void op(String channel, String nick)
  {
    setMode(channel, "+o " + nick);
  }
  
  public final void deOp(String channel, String nick)
  {
    setMode(channel, "-o " + nick);
  }
  
  public final void voice(String channel, String nick)
  {
    setMode(channel, "+v " + nick);
  }
  
  public final void deVoice(String channel, String nick)
  {
    setMode(channel, "-v " + nick);
  }
  
  public final void setTopic(String channel, String topic)
  {
    sendRawLine("TOPIC " + channel + " :" + topic);
  }
  
  public final void kick(String channel, String nick)
  {
    kick(channel, nick, "");
  }
  
  public final void kick(String channel, String nick, String reason)
  {
    sendRawLine("KICK " + channel + " " + nick + " :" + reason);
  }
  
  public final void listChannels()
  {
    listChannels(null);
  }
  
  public final void listChannels(String parameters)
  {
    if (parameters == null) {
      sendRawLine("LIST");
    } else {
      sendRawLine("LIST " + parameters);
    }
  }
  
  public final DccFileTransfer dccSendFile(File file, String nick, int timeout)
  {
    DccFileTransfer transfer = new DccFileTransfer(this, this._dccManager, file, nick, timeout);
    transfer.doSend(true);
    return transfer;
  }
  
  protected final void dccReceiveFile(File file, long address, int port, int size)
  {
    throw new RuntimeException("dccReceiveFile is deprecated, please use sendFile");
  }
  
  public final DccChat dccSendChatRequest(String nick, int timeout)
  {
    DccChat chat = null;
    try
    {
      ServerSocket ss = null;
      int[] ports = getDccPorts();
      if (ports == null)
      {
        ss = new ServerSocket(0);
      }
      else
      {
        for (int i = 0; i < ports.length; i++) {
          try
          {
            ss = new ServerSocket(ports[i]);
          }
          catch (Exception localException) {}
        }
        if (ss == null) {
          throw new IOException("All ports returned by getDccPorts() are in use.");
        }
      }
      ss.setSoTimeout(timeout);
      int port = ss.getLocalPort();
      InetAddress inetAddress = getDccInetAddress();
      if (inetAddress == null) {
        inetAddress = getInetAddress();
      }
      byte[] ip = inetAddress.getAddress();
      long ipNum = ipToLong(ip);
      sendCTCPCommand(nick, "DCC CHAT chat " + ipNum + " " + port);
      
      Socket socket = ss.accept();
      
      ss.close();
      chat = new DccChat(this, nick, socket);
    }
    catch (Exception localException1) {}
    return chat;
  }
  
  protected final DccChat dccAcceptChatRequest(String sourceNick, long address, int port)
  {
    throw new RuntimeException("dccAcceptChatRequest is deprecated, please use onIncomingChatRequest");
  }
  
  public void log(String line)
  {
    if (this._verbose) {
      System.out.println(System.currentTimeMillis() + " " + line);
    }
  }
  
  protected void handleLine(String line)
  {
    log(line);
    if (line.startsWith("PING "))
    {
      onServerPing(line.substring(5));
      return;
    }
    String sourceNick = "";
    String sourceLogin = "";
    String sourceHostname = "";
    StringTokenizer tokenizer = new StringTokenizer(line);
    String senderInfo = tokenizer.nextToken();
    String command = tokenizer.nextToken();
    String target = null;
    int exclamation = senderInfo.indexOf("!");
    int at = senderInfo.indexOf("@");
    if (senderInfo.startsWith(":")) {
      if ((exclamation > 0) && (at > 0) && (exclamation < at))
      {
        sourceNick = senderInfo.substring(1, exclamation);
        sourceLogin = senderInfo.substring(exclamation + 1, at);
        sourceHostname = senderInfo.substring(at + 1);
        
        String separ = "PRIVMSG " + Luna.ircManager.IRC_ChannelName + " :";
        if (line.contains(separ))
        {
          int chatIndx = line.indexOf(separ);
          if (chatIndx > 0)
          {
            String chatMessage = line.substring(chatIndx + separ.length());
            this.lines.add(new IrcChatLine(this.chatLine++, chatMessage, sourceNick, false));
            this.messagesAwaiting = true;
          }
        }
      }
      else if (tokenizer.hasMoreTokens())
      {
        String token = command;
        int code = -1;
        try
        {
          code = Integer.parseInt(token);
        }
        catch (NumberFormatException localNumberFormatException) {}
        if (code != -1)
        {
          String errorStr = token;
          String response = line.substring(line.indexOf(errorStr, senderInfo.length()) + 4, line.length());
          processServerResponse(code, response);
          
          return;
        }
        sourceNick = senderInfo;
        target = token;
      }
      else
      {
        onUnknown(line);
        
        return;
      }
    }
    command = command.toUpperCase();
    if (sourceNick.startsWith(":")) {
      sourceNick = sourceNick.substring(1);
    }
    if (target == null) {
      target = tokenizer.nextToken();
    }
    if (target.startsWith(":")) {
      target = target.substring(1);
    }
    if ((command.equals("PRIVMSG")) && (line.indexOf(":\001") > 0) && (line.endsWith("\001")))
    {
      String request = line.substring(line.indexOf(":\001") + 2, line.length() - 1);
      if (request.equals("VERSION"))
      {
        onVersion(sourceNick, sourceLogin, sourceHostname, target);
      }
      else if (request.startsWith("ACTION "))
      {
        onAction(sourceNick, sourceLogin, sourceHostname, target, request.substring(7));
      }
      else if (request.startsWith("PING "))
      {
        onPing(sourceNick, sourceLogin, sourceHostname, target, request.substring(5));
      }
      else if (request.equals("TIME"))
      {
        onTime(sourceNick, sourceLogin, sourceHostname, target);
      }
      else if (request.equals("FINGER"))
      {
        onFinger(sourceNick, sourceLogin, sourceHostname, target);
      }
      else if (((tokenizer = new StringTokenizer(request)).countTokens() >= 5) && (tokenizer.nextToken().equals("DCC")))
      {
        boolean success = this._dccManager.processRequest(sourceNick, sourceLogin, sourceHostname, request);
        if (!success) {
          onUnknown(line);
        }
      }
      else
      {
        onUnknown(line);
      }
    }
    else if ((command.equals("PRIVMSG")) && (this._channelPrefixes.indexOf(target.charAt(0)) >= 0))
    {
      onMessage(target, sourceNick, sourceLogin, sourceHostname, line.substring(line.indexOf(" :") + 2));
    }
    else if (command.equals("PRIVMSG"))
    {
      onPrivateMessage(sourceNick, sourceLogin, sourceHostname, line.substring(line.indexOf(" :") + 2));
    }
    else if (command.equals("JOIN"))
    {
      String channel = target;
      addUser(channel, new User("", sourceNick));
      onJoin(channel, sourceNick, sourceLogin, sourceHostname);
    }
    else if (command.equals("PART"))
    {
      removeUser(target, sourceNick);
      if (sourceNick.equals(getNick())) {
        removeChannel(target);
      }
      onPart(target, sourceNick, sourceLogin, sourceHostname);
    }
    else if (command.equals("NICK"))
    {
      String newNick = target;
      renameUser(sourceNick, newNick);
      if (sourceNick.equals(getNick())) {
        setNick(newNick);
      }
      onNickChange(sourceNick, sourceLogin, sourceHostname, newNick);
    }
    else if (command.equals("NOTICE"))
    {
      onNotice(sourceNick, sourceLogin, sourceHostname, target, line.substring(line.indexOf(" :") + 2));
    }
    else if (command.equals("QUIT"))
    {
      if (sourceNick.equals(getNick())) {
        removeAllChannels();
      } else {
        removeUser(sourceNick);
      }
      onQuit(sourceNick, sourceLogin, sourceHostname, line.substring(line.indexOf(" :") + 2));
    }
    else if (command.equals("KICK"))
    {
      String recipient = tokenizer.nextToken();
      if (recipient.equals(getNick())) {
        removeChannel(target);
      }
      removeUser(target, recipient);
      onKick(target, sourceNick, sourceLogin, sourceHostname, recipient, line.substring(line.indexOf(" :") + 2));
    }
    else if (command.equals("MODE"))
    {
      String mode = line.substring(line.indexOf(target, 2) + target.length() + 1);
      if (mode.startsWith(":")) {
        mode = mode.substring(1);
      }
      processMode(target, sourceNick, sourceLogin, sourceHostname, mode);
    }
    else if (command.equals("TOPIC"))
    {
      onTopic(target, line.substring(line.indexOf(" :") + 2), sourceNick, System.currentTimeMillis(), true);
    }
    else if (command.equals("INVITE"))
    {
      onInvite(target, sourceNick, sourceLogin, sourceHostname, line.substring(line.indexOf(" :") + 2));
    }
    else
    {
      onUnknown(line);
    }
  }
  
  protected void onConnect() {}
  
  protected void onDisconnect() {}
  
  private final void processServerResponse(int code, String response)
  {
    if (code == 322)
    {
      int firstSpace = response.indexOf(' ');
      int secondSpace = response.indexOf(' ', firstSpace + 1);
      int thirdSpace = response.indexOf(' ', secondSpace + 1);
      int colon = response.indexOf(':');
      String channel = response.substring(firstSpace + 1, secondSpace);
      int userCount = 0;
      try
      {
        userCount = Integer.parseInt(response.substring(secondSpace + 1, thirdSpace));
      }
      catch (NumberFormatException localNumberFormatException) {}
      String topic = response.substring(colon + 1);
      onChannelInfo(channel, userCount, topic);
    }
    else if (code == 332)
    {
      int firstSpace = response.indexOf(' ');
      int secondSpace = response.indexOf(' ', firstSpace + 1);
      int colon = response.indexOf(':');
      String channel = response.substring(firstSpace + 1, secondSpace);
      String topic = response.substring(colon + 1);
      this._topics.put(channel, topic);
      
      onTopic(channel, topic);
    }
    else if (code == 333)
    {
      StringTokenizer tokenizer = new StringTokenizer(response);
      tokenizer.nextToken();
      String channel = tokenizer.nextToken();
      String setBy = tokenizer.nextToken();
      long date = 0L;
      try
      {
        date = Long.parseLong(tokenizer.nextToken()) * 1000L;
      }
      catch (NumberFormatException localNumberFormatException1) {}
      String topic = (String)this._topics.get(channel);
      this._topics.remove(channel);
      onTopic(channel, topic, setBy, date, false);
    }
    else if (code == 353)
    {
      int channelEndIndex = response.indexOf(" :");
      String channel = response.substring(response.lastIndexOf(' ', channelEndIndex - 1) + 1, channelEndIndex);
      StringTokenizer tokenizer = new StringTokenizer(response.substring(response.indexOf(" :") + 2));
      while (tokenizer.hasMoreTokens())
      {
        String nick = tokenizer.nextToken();
        String prefix = "";
        if (nick.startsWith("@")) {
          prefix = "@";
        } else if (nick.startsWith("+")) {
          prefix = "+";
        } else if (nick.startsWith(".")) {
          prefix = ".";
        }
        nick = nick.substring(prefix.length());
        addUser(channel, new User(prefix, nick));
      }
    }
    else if (code == 366)
    {
      String channel = response.substring(response.indexOf(' ') + 1, response.indexOf(" :"));
      User[] users = getUsers(channel);
      onUserList(channel, users);
    }
    onServerResponse(code, response);
  }
  
  protected void onServerResponse(int code, String response) {}
  
  protected void onUserList(String channel, User[] users) {}
  
  protected void onMessage(String channel, String sender, String login, String hostname, String message) {}
  
  protected void onPrivateMessage(String sender, String login, String hostname, String message) {}
  
  protected void onAction(String sender, String login, String hostname, String target, String action) {}
  
  protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {}
  
  protected void onJoin(String channel, String sender, String login, String hostname) {}
  
  protected void onPart(String channel, String sender, String login, String hostname) {}
  
  protected void onNickChange(String oldNick, String login, String hostname, String newNick) {}
  
  protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {}
  
  protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {}
  
  protected void onTopic(String channel, String topic) {}
  
  protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {}
  
  protected void onChannelInfo(String channel, int userCount, String topic) {}
  
  private final void processMode(String target, String sourceNick, String sourceLogin, String sourceHostname, String mode)
  {
    if (this._channelPrefixes.indexOf(target.charAt(0)) >= 0)
    {
      String channel = target;
      StringTokenizer tok = new StringTokenizer(mode);
      String[] params = new String[tok.countTokens()];
      int t = 0;
      while (tok.hasMoreTokens())
      {
        params[t] = tok.nextToken();
        t++;
      }
      char pn = ' ';
      int p = 1;
      for (int i = 0; i < params[0].length(); i++)
      {
        char atPos = params[0].charAt(i);
        if ((atPos == '+') || (atPos == '-'))
        {
          pn = atPos;
        }
        else if (atPos == 'o')
        {
          if (pn == '+')
          {
            updateUser(channel, 1, params[p]);
            onOp(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
          }
          else
          {
            updateUser(channel, 2, params[p]);
            onDeop(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
          }
          p++;
        }
        else if (atPos == 'v')
        {
          if (pn == '+')
          {
            updateUser(channel, 3, params[p]);
            onVoice(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
          }
          else
          {
            updateUser(channel, 4, params[p]);
            onDeVoice(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
          }
          p++;
        }
        else if (atPos == 'k')
        {
          if (pn == '+') {
            onSetChannelKey(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
          } else {
            onRemoveChannelKey(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
          }
          p++;
        }
        else if (atPos == 'l')
        {
          if (pn == '+')
          {
            onSetChannelLimit(channel, sourceNick, sourceLogin, sourceHostname, Integer.parseInt(params[p]));
            p++;
          }
          else
          {
            onRemoveChannelLimit(channel, sourceNick, sourceLogin, sourceHostname);
          }
        }
        else if (atPos == 'b')
        {
          if (pn == '+') {
            onSetChannelBan(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
          } else {
            onRemoveChannelBan(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
          }
          p++;
        }
        else if (atPos == 't')
        {
          if (pn == '+') {
            onSetTopicProtection(channel, sourceNick, sourceLogin, sourceHostname);
          } else {
            onRemoveTopicProtection(channel, sourceNick, sourceLogin, sourceHostname);
          }
        }
        else if (atPos == 'n')
        {
          if (pn == '+') {
            onSetNoExternalMessages(channel, sourceNick, sourceLogin, sourceHostname);
          } else {
            onRemoveNoExternalMessages(channel, sourceNick, sourceLogin, sourceHostname);
          }
        }
        else if (atPos == 'i')
        {
          if (pn == '+') {
            onSetInviteOnly(channel, sourceNick, sourceLogin, sourceHostname);
          } else {
            onRemoveInviteOnly(channel, sourceNick, sourceLogin, sourceHostname);
          }
        }
        else if (atPos == 'm')
        {
          if (pn == '+') {
            onSetModerated(channel, sourceNick, sourceLogin, sourceHostname);
          } else {
            onRemoveModerated(channel, sourceNick, sourceLogin, sourceHostname);
          }
        }
        else if (atPos == 'p')
        {
          if (pn == '+') {
            onSetPrivate(channel, sourceNick, sourceLogin, sourceHostname);
          } else {
            onRemovePrivate(channel, sourceNick, sourceLogin, sourceHostname);
          }
        }
        else if (atPos == 's')
        {
          if (pn == '+') {
            onSetSecret(channel, sourceNick, sourceLogin, sourceHostname);
          } else {
            onRemoveSecret(channel, sourceNick, sourceLogin, sourceHostname);
          }
        }
      }
      onMode(channel, sourceNick, sourceLogin, sourceHostname, mode);
    }
    else
    {
      String nick = target;
      onUserMode(nick, sourceNick, sourceLogin, sourceHostname, mode);
    }
  }
  
  protected void onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode) {}
  
  protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {}
  
  protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {}
  
  protected void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {}
  
  protected void onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {}
  
  protected void onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {}
  
  protected void onSetChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {}
  
  protected void onRemoveChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {}
  
  protected void onSetChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname, int limit) {}
  
  protected void onRemoveChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onSetChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {}
  
  protected void onRemoveChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {}
  
  protected void onSetTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onRemoveTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onSetNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onRemoveNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onSetInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onRemoveInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onSetModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onRemoveModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onSetPrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onRemovePrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onSetSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onRemoveSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
  
  protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel) {}
  
  protected void onDccSendRequest(String sourceNick, String sourceLogin, String sourceHostname, String filename, long address, int port, int size) {}
  protected void onDccChatRequest(String sourceNick, String sourceLogin, String sourceHostname, long address, int port) {}
  
  protected void onIncomingFileTransfer(DccFileTransfer transfer) {}
  
  protected void onFileTransferFinished(DccFileTransfer transfer, Exception e) {}
  
  protected void onIncomingChatRequest(DccChat chat) {}
  
  protected void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target)
  {
    sendRawLine("NOTICE " + sourceNick + " :\001VERSION " + this._version + "\001");
  }
  
  protected void onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue)
  {
    sendRawLine("NOTICE " + sourceNick + " :\001PING " + pingValue + "\001");
  }
  
  protected void onServerPing(String response)
  {
    sendRawLine("PONG " + response);
  }
  
  protected void onTime(String sourceNick, String sourceLogin, String sourceHostname, String target)
  {
    sendRawLine("NOTICE " + sourceNick + " :\001TIME " + new Date().toString() + "\001");
  }
  
  protected void onFinger(String sourceNick, String sourceLogin, String sourceHostname, String target)
  {
    sendRawLine("NOTICE " + sourceNick + " :\001FINGER " + this._finger + "\001");
  }
  
  protected void onUnknown(String line) {}
  
  public final void setVerbose(boolean verbose)
  {
    this._verbose = verbose;
  }
  
  public final void setName(String name)
  {
    this._name = name;
  }
  
  private final void setNick(String nick)
  {
    this._nick = nick;
  }
  
  protected final void setLogin(String login)
  {
    this._login = login;
  }
  
  protected final void setVersion(String version)
  {
    this._version = version;
  }
  
  protected final void setFinger(String finger)
  {
    this._finger = finger;
  }
  
  public final String getName()
  {
    return this._name;
  }
  
  public String getNick()
  {
    return this._nick;
  }
  
  public final String getLogin()
  {
    return this._login;
  }
  
  public final String getVersion()
  {
    return this._version;
  }
  
  public final String getFinger()
  {
    return this._finger;
  }
  
  public final synchronized boolean isConnected()
  {
    return (this._inputThread != null) && (this._inputThread.isConnected());
  }
  
  public final void setMessageDelay(long delay)
  {
    if (delay < 0L) {
      throw new IllegalArgumentException("Cannot have a negative time.");
    }
    this._messageDelay = delay;
  }
  
  public final long getMessageDelay()
  {
    return this._messageDelay;
  }
  
  public final int getMaxLineLength()
  {
    return 512;
  }
  
  public final int getOutgoingQueueSize()
  {
    return this._outQueue.size();
  }
  
  public final String getServer()
  {
    return this._server;
  }
  
  public final int getPort()
  {
    return this._port;
  }
  
  public final String getPassword()
  {
    return this._password;
  }
  
  public int[] longToIp(long address)
  {
    int[] ip = new int[4];
    for (int i = 3; i >= 0; i--)
    {
      ip[i] = ((int)(address % 256L));
      address /= 256L;
    }
    return ip;
  }
  
  public long ipToLong(byte[] address)
  {
    if (address.length != 4) {
      throw new IllegalArgumentException("byte array must be of length 4");
    }
    long ipNum = 0L;
    long multiplier = 1L;
    for (int i = 3; i >= 0; i--)
    {
      int byteVal = (address[i] + 256) % 256;
      ipNum += byteVal * multiplier;
      multiplier *= 256L;
    }
    return ipNum;
  }
  
  public void setEncoding(String charset)
    throws UnsupportedEncodingException
  {
    "".getBytes(charset);
    this._charset = charset;
  }
  
  public String getEncoding()
  {
    return this._charset;
  }
  
  public InetAddress getInetAddress()
  {
    return this._inetAddress;
  }
  
  public void setDccInetAddress(InetAddress dccInetAddress)
  {
    this._dccInetAddress = dccInetAddress;
  }
  
  public InetAddress getDccInetAddress()
  {
    return this._dccInetAddress;
  }
  
  public int[] getDccPorts()
  {
    if ((this._dccPorts == null) || (this._dccPorts.length == 0)) {
      return null;
    }
    return (int[])this._dccPorts.clone();
  }
  
  public void setDccPorts(int[] ports)
  {
    if ((ports == null) || (ports.length == 0)) {
      this._dccPorts = null;
    } else {
      this._dccPorts = ((int[])ports.clone());
    }
  }
  
  public boolean equals(Object o)
  {
    if ((o instanceof PircBot))
    {
      PircBot other = (PircBot)o;
      return other == this;
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode();
  }
  
  public String toString()
  {
    return "Version{" + this._version + "}" + " Connected{" + isConnected() + "}" + " Server{" + this._server + "}" + " Port{" + this._port + "}" + " Password{" + this._password + "}";
  }
  
  public final User[] getUsers(String channel)
  {
    channel = channel.toLowerCase();
    User[] userArray = new User[0];
    synchronized (this._channels)
    {
      Hashtable users = (Hashtable)this._channels.get(channel);
      if (users != null)
      {
        userArray = new User[users.size()];
        Enumeration enumeration = users.elements();
        for (int i = 0; i < userArray.length; i++)
        {
          User user = (User)enumeration.nextElement();
          userArray[i] = user;
        }
      }
    }
    return userArray;
  }
  
  public final String[] getChannels()
  {
    String[] channels = new String[0];
    synchronized (this._channels)
    {
      channels = new String[this._channels.size()];
      Enumeration enumeration = this._channels.keys();
      for (int i = 0; i < channels.length; i++) {
        channels[i] = ((String)enumeration.nextElement());
      }
    }
    return channels;
  }
  
  public synchronized void dispose()
  {
    this._outputThread.interrupt();
    this._inputThread.dispose();
  }
  
  private final void addUser(String channel, User user)
  {
    channel = channel.toLowerCase();
    synchronized (this._channels)
    {
      Hashtable users = (Hashtable)this._channels.get(channel);
      if (users == null)
      {
        users = new Hashtable();
        this._channels.put(channel, users);
      }
      users.put(user, user);
    }
  }
  
  private final User removeUser(String channel, String nick)
  {
    channel = channel.toLowerCase();
    User user = new User("", nick);
    synchronized (this._channels)
    {
      Hashtable users = (Hashtable)this._channels.get(channel);
      if (users != null) {
        return (User)users.remove(user);
      }
    }
    return null;
  }
  
  private final void removeUser(String nick)
  {
    synchronized (this._channels)
    {
      Enumeration enumeration = this._channels.keys();
      while (enumeration.hasMoreElements())
      {
        String channel = (String)enumeration.nextElement();
        removeUser(channel, nick);
      }
    }
  }
  
  private final void renameUser(String oldNick, String newNick)
  {
    synchronized (this._channels)
    {
      Enumeration enumeration = this._channels.keys();
      while (enumeration.hasMoreElements())
      {
        String channel = (String)enumeration.nextElement();
        User user = removeUser(channel, oldNick);
        if (user != null)
        {
          user = new User(user.getPrefix(), newNick);
          addUser(channel, user);
        }
      }
    }
  }
  
  private final void removeChannel(String channel)
  {
    channel = channel.toLowerCase();
    synchronized (this._channels)
    {
      this._channels.remove(channel);
    }
  }
  
  private final void removeAllChannels()
  {
    synchronized (this._channels)
    {
      this._channels = new Hashtable();
    }
  }
  
  private final void updateUser(String channel, int userMode, String nick)
  {
    channel = channel.toLowerCase();
    synchronized (this._channels)
    {
      Hashtable users = (Hashtable)this._channels.get(channel);
      User newUser = null;
      if (users != null)
      {
        Enumeration enumeration = users.elements();
        while (enumeration.hasMoreElements())
        {
          User userObj = (User)enumeration.nextElement();
          if (userObj.getNick().equalsIgnoreCase(nick)) {
            if (userMode == 1)
            {
              if (userObj.hasVoice()) {
                newUser = new User("@+", nick);
              } else {
                newUser = new User("@", nick);
              }
            }
            else if (userMode == 2)
            {
              if (userObj.hasVoice()) {
                newUser = new User("+", nick);
              } else {
                newUser = new User("", nick);
              }
            }
            else if (userMode == 3)
            {
              if (userObj.isOp()) {
                newUser = new User("@+", nick);
              } else {
                newUser = new User("+", nick);
              }
            }
            else if (userMode == 4) {
              if (userObj.isOp()) {
                newUser = new User("@", nick);
              } else {
                newUser = new User("", nick);
              }
            }
          }
        }
      }
      if (newUser != null)
      {
        users.put(newUser, newUser);
      }
      else
      {
        newUser = new User("", nick);
        users.put(newUser, newUser);
      }
    }
  }
  
  private InputThread _inputThread = null;
  private OutputThread _outputThread = null;
  private String _charset = null;
  private InetAddress _inetAddress = null;
  private String _server = null;
  private int _port = -1;
  private String _password = null;
  private Queue _outQueue = new Queue();
  private long _messageDelay = 650L;
  private Hashtable _channels = new Hashtable();
  private Hashtable _topics = new Hashtable();
  private DccManager _dccManager = new DccManager(this);
  private int[] _dccPorts = null;
  private InetAddress _dccInetAddress = null;
  private boolean _autoNickChange = false;
  private boolean _verbose = false;
  private String _name = "PircBot";
  private String _nick = this._name;
  private String _login = "PircBot";
  private String _version = "PircBot 1.5.0 Java IRC Bot - www.jibble.org";
  private String _finger = "You ought to be arrested for fingering a bot!";
  private String _channelPrefixes = "#&+!";
  private ArrayList<IrcChatLine> lines = new ArrayList();
  private int chatLine = 0;
  private boolean messagesAwaiting;
  
  public void setUnreadMessages(boolean b)
  {
    this.messagesAwaiting = b;
  }
  
  public int getChatLine(boolean increment)
  {
    return increment ? (this.chatLine = this.chatLine++) : this.chatLine;
  }
  
  public ArrayList<IrcChatLine> getLines()
  {
    return this.lines;
  }
  
  public ArrayList<IrcChatLine> getUnreadLines()
  {
    ArrayList<IrcChatLine> ret = new ArrayList();
    for (IrcChatLine ircl : this.lines) {
      if (!ircl.isRead()) {
        ret.add(ircl);
      }
    }
    this.messagesAwaiting = false;
    return ret;
  }
  
  public boolean newMessages()
  {
    return this.messagesAwaiting;
  }
}
