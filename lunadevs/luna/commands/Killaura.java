package lunadevs.luna.commands;

import lunadevs.luna.command.Command;
import lunadevs.luna.main.Luna;

public class Killaura extends Command{


	
	@Override
	public String getAlias() {
		return "killaura";
	}

	@Override
	public String getDescription() {
		return "Changes killaura settings";
	}

	@Override
	public String getSyntax() {
		return "-killaura";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(args[0].equalsIgnoreCase("switch")){
			    lunadevs.luna.module.combat.Killaura.mode=0;
				Luna.addChatMessage("Killaura mode set to: Switch");

		}
		if(args[0].equalsIgnoreCase("tick")){
			lunadevs.luna.module.combat.Killaura.mode=1;
			Luna.addChatMessage("Killaura mode set to: Tick");
		}
		if(args[0].equalsIgnoreCase("block")){
		      if (args[1].equalsIgnoreCase("true"))
		      {
				  lunadevs.luna.module.combat.Killaura.block = true;
		        Luna.addChatMessage("KillauraBlock: True");
		      }
		      if (args[1].equalsIgnoreCase("false"))
		      {
				  lunadevs.luna.module.combat.Killaura.block = false;
		        Luna.addChatMessage("KillauraBlock: False");
		      }
		}
		if(args[0].equalsIgnoreCase("friend")){
		      if (args[1].equalsIgnoreCase("true"))
		      {
				  lunadevs.luna.module.combat.Killaura.friend = true;
		        Luna.addChatMessage("KillauraFriend: True");
		      }
		      if (args[1].equalsIgnoreCase("false"))
		      {
				  lunadevs.luna.module.combat.Killaura.friend = false;
		        Luna.addChatMessage("KillauraFriend: False");
		      }
		}
		if(args[0].equalsIgnoreCase("range")){

		      lunadevs.luna.module.combat.Killaura.range = Float.parseFloat(args[1]);
		      Luna.addChatMessage("KillauraRange: " + lunadevs.luna.module.combat.Killaura.range);
		}
		if(args[0].equalsIgnoreCase("speed")){
		      lunadevs.luna.module.combat.Killaura.speed = Float.parseFloat(args[1]);
		      Luna.addChatMessage("KillauraSpeed: " + lunadevs.luna.module.combat.Killaura.speed);
		}
		if(args[0].equalsIgnoreCase("lock")){
		      if (args[1].equalsIgnoreCase("true"))
		      {
				  lunadevs.luna.module.combat.Killaura.lock = true;
		        Luna.addChatMessage("KillauraLock: True");
		      }
		      if (args[1].equalsIgnoreCase("false"))
		      {
				  lunadevs.luna.module.combat.Killaura.lock = false;
		        Luna.addChatMessage("KillauraLock: False");
		      }
		}
	}

}
