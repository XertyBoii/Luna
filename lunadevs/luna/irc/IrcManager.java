package lunadevs.luna.irc;

import net.minecraft.server.*;
import java.io.*;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import com.zCore.Core.zCore;

public class IrcManager extends PircBot
{
    private final String IRC_HostName = "irc.mibbit.net";
    private final int IRC_HostPort = 6667;
    public static final String IRC_ChannelName = "#LunaClient";
    private static String username;
    
    public IrcManager(String username) {
        try {
            final String firstname = username.substring(0, 1);
            final int i = Integer.parseInt(firstname);
            System.out.println("[IRC] Usernames Cannont begin with numbers");
            username = "MC" + username;
        }
        catch (NumberFormatException ex) {}
        IrcManager.username = username;
    }
    
    public void connect() {
        this.setAutoNickChange(true);
        this.setName(IrcManager.username);
        this.changeNick(IrcManager.username);
        System.out.println("Connecting To IRC");
        try {
            this.connect(IRC_HostName, IRC_HostPort);
        }
        catch (NickAlreadyInUseException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (IrcException e3) {
            e3.printStackTrace();
        }
        System.out.println("Connecting to #LunaClient > 6667 < IRC Server");
        this.joinChannel(this.IRC_ChannelName);
        System.out.println("Connected!");
    }
}
