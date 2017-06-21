package lunadevs.luna.commands;

import lunadevs.luna.command.Command;
import lunadevs.luna.main.Parallaxa;

//coded by faith

public class Say extends Command{

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "say";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "obvious :/";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "-say <something>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
	    if (args.length > 1) {
	       Parallaxa.sendChatMessage(command.substring(args[0].length()));
	      } else {
	        Parallaxa.addChatMessage("ERROR! Can't send null messages.");
	      }
	    }
	}

	
	

