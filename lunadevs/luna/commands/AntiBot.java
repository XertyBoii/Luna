package lunadevs.luna.commands;

import lunadevs.luna.command.Command;
import lunadevs.luna.main.Parallaxa;

public class AntiBot extends Command{

	@Override
	public String getAlias() {
		return "antibot";
	}

	@Override
	public String getDescription() {
		return "Changes AntiBot settings";
	}

	@Override
	public String getSyntax() {
		return "-antibot";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if (args[0].equalsIgnoreCase("advanced")){
			Parallaxa.addChatMessage("AntiBot mode set to Advanced");
			lunadevs.luna.module.combat.AntiBot.Watchdog = false;
			lunadevs.luna.module.combat.AntiBot.GWEN = true;
		}
		if(args[0].equalsIgnoreCase("watchdog")){
			Parallaxa.addChatMessage("AntiBot mode set to WatchDog");
			lunadevs.luna.module.combat.AntiBot.Watchdog = true;
			lunadevs.luna.module.combat.AntiBot.GWEN = false;
		}
	}

}
