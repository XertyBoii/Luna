package lunadevs.luna.commands;

import lunadevs.luna.command.Command;
import lunadevs.luna.main.Parallaxa;

//Coded By Faith.

public class ClientBuild extends Command{

	@Override
	public String getAlias() {
		return "build";
	}

	@Override
	public String getDescription() {
		return "Gives the user the build number of the client the user is using.";
	}

	@Override
	public String getSyntax() {
		return "-build <tells user what build>";
	}

	String b = "You are using: b" + Parallaxa.CLIENT_BUILD + " " + "(" + Parallaxa.CLIENT_BUILD + ")" ;
	
	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(Parallaxa.onSendChatMessage(command)) {
			Parallaxa.addChatMessage(b);
		}
		
	}

}
