package lunadevs.luna.commands;

import lunadevs.luna.command.Command;
import lunadevs.luna.main.Parallaxa;

//Made By Faith.
//First Module/command made by me on this client :PPP

public class Developers extends Command{


	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "devs";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Gives the user a description of the developers of The client.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "Do .devs";
	}

	String devs = "@ZiTROXClient, @Mega_Mixer74";
	
	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(Parallaxa.onSendChatMessage(command)) {
			Parallaxa.addChatMessage(devs);
		}
		
	}

	
	
	
	
}
