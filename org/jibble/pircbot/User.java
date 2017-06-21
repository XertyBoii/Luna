package org.jibble.pircbot;

public class User
{
  public int timer;
  public int messages;
  public boolean muted;
  private String _prefix;
  private String _nick;
  private String _lowerNick;
  
  User(String prefix, String nick)
  {
    this._prefix = prefix;
    this._nick = nick;
    this._lowerNick = nick.toLowerCase();
  }
  
  public String getPrefix()
  {
    return this._prefix;
  }
  
  public boolean isOp()
  {
    return this._prefix.indexOf('@') >= 0;
  }
  
  public boolean hasVoice()
  {
    return this._prefix.indexOf('+') >= 0;
  }
  
  public String getNick()
  {
    return this._nick;
  }
  
  public String toString()
  {
    return getPrefix() + getNick();
  }
  
  public boolean equals(String nick)
  {
    return nick.toLowerCase().equals(this._lowerNick);
  }
  
  public boolean equals(Object o)
  {
    if ((o instanceof User))
    {
      User other = (User)o;
      return other._lowerNick.equals(this._lowerNick);
    }
    return false;
  }
  
  public int hashCode()
  {
    return this._lowerNick.hashCode();
  }
  
  public int compareTo(Object o)
  {
    if ((o instanceof User))
    {
      User other = (User)o;
      return other._lowerNick.compareTo(this._lowerNick);
    }
    return -1;
  }
}
