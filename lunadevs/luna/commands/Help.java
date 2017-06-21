package lunadevs.luna.commands;

import com.zCore.Core.zCore;

import lunadevs.luna.command.Command;

public class Help extends Command{

	@Override
	
	public String getAlias() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Find out about the client";
	}

	@Override
	public String getSyntax() {
		return "-help";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		zCore.addChatMessageP("-t <module>");
		zCore.addChatMessageP("-bind set/del <module>");
		zCore.addChatMessageP("-killaura <block/friend/lock/speed/range> <true/false>");
		zCore.addChatMessageP("-killaura <tick/switch>");
		zCore.addChatMessageP("-vclip <blocks>");
		zCore.addChatMessageP("-hclip <blocks>");
		zCore.addChatMessageP("-friend add/del <name>");
		zCore.addChatMessageP("-longjump <old/new>");
        zCore.addChatMessageP("-timer speed <value>");
        zCore.addChatMessageP("-devs");
        zCore.addChatMessageP("-build");
        zCore.addChatMessageP("-clearchat");
        
	}

}
