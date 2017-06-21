package lunadevs.luna.commands;

import lunadevs.luna.command.Command;
import lunadevs.luna.friend.FriendManager;
import lunadevs.luna.main.Parallaxa;

public class Friend extends Command{

	@Override
	public String getAlias() {
		return "friend";
	}

	@Override
	public String getDescription() {
		return "adds a friend";
	}

	@Override
	public String getSyntax() {
		return "-friend add <name> | .friend del <name>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(args[0].equalsIgnoreCase("add")){
			if (FriendManager.isFriend( args[1])){
				Parallaxa.addChatMessage(args[1] + " Is Already Your Friend");
				return;
			}
			String alias = args[1];
			FriendManager.addFriend(args[1], alias);
		    Parallaxa.addChatMessage("Added " + args[1] + " As Friend");
		} else if (args[0].equalsIgnoreCase("del"))
	    {
			if (FriendManager.isFriend(args[1])){
		        FriendManager.removeFriend(args[1]);
		        Parallaxa.addChatMessage("Removed " + args[1] + " As friend");
			}else{
				Parallaxa.addChatMessage(args[1] + " Is Not Your Friend");
			}
		      }
	    }
	}
	    