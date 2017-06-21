package lunadevs.luna.module.player;

import org.jibble.pircbot.User;
import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.zCore.Core.zCore;

import lunadevs.luna.category.Category;
import lunadevs.luna.events.TickEvent;
import lunadevs.luna.irc.IrcManager;
import lunadevs.luna.main.Luna;
import lunadevs.luna.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import lunadevs.luna.irc.IrcChatLine;

public class IRC extends Module{

	private static IrcManager irc = Luna.ircManager;
	
	public IRC() {
		super("IRC", Keyboard.KEY_NONE, Category.PLAYER, false);
	}
	@EventTarget
	public void onEvent(TickEvent event){
		if(!this.isEnabled) return;
	      for (IrcChatLine irc : Luna.ircManager.getUnreadLines())
	      {
	        Luna.addIRCMessage("\2478[\2477" +irc.getSender()+ "\2478]\2477 " + irc.getLine());
	        irc.setRead(true);
	        }
	}
	
	@Override
	public void onEnable() {
		Luna.addIRCMessage("Use '@Message' to chat");
		Luna.addIRCMessage("You will be know as: \2478" + Luna.ircManager.getNick());
		super.onEnable();
	}

	@Override
	public String getValue() {
		return null;
	}

}
