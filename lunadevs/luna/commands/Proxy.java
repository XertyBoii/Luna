package lunadevs.luna.commands;

import lunadevs.luna.command.Command;
import lunadevs.luna.main.Parallaxa;

public class Proxy extends Command{

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "proxyisgay";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "tells the user that proxy is gay.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "-proxyisgay";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(Parallaxa.onSendChatMessage(command)) {
			Parallaxa.addChatMessage("Proxy is indeed gay!");
		}
	}

}
