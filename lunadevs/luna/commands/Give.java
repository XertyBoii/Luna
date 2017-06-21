package lunadevs.luna.commands;

import lunadevs.luna.command.Command;
import lunadevs.luna.main.Parallaxa;
import lunadevs.luna.utils.Inventory;
import lunadevs.luna.utils.faithsminiutils.Wrapper;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

//Coded by faith.

public class Give extends Command {

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "give";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "gives the play item of choice.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "-give <item> <randomnumber / -give <randomnumber> <item>";
	}

	  private static Item getItemByText(String itemName)
			    throws NumberInvalidException
			  {
			    Item item = (Item)Item.itemRegistry.getObject(itemName);
			    if (item == null) {
			      try
			      {
			        item = Item.getItemById(Integer.parseInt(itemName));
			      }
			      catch (NumberFormatException localNumberFormatException) {}
			    }
			    if (item == null) {
			      throw new NumberInvalidException("commands.give.notFound", new Object[] { itemName });
			    }
			    return item;
			  }
			  
			  private static int parseInt(String integer)
			    throws NumberInvalidException
			  {
			    try
			    {
			      return Integer.parseInt(integer);
			    }
			    catch (NumberFormatException exception)
			    {
			      throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { integer });
			    }
			  }
			  
			  private static int parseIntBounded(String integer, int min, int max)
			    throws NumberInvalidException
			  {
			    int parsedInt = parseInt(integer);
			    if (parsedInt < min) {
			      throw new NumberInvalidException("commands.generic.num.tooSmall", 
			        new Object[] { Integer.valueOf(parsedInt), Integer.valueOf(min) });
			    }
			    if (parsedInt > max) {
			      throw new NumberInvalidException("commands.generic.num.tooBig", 
			        new Object[] { Integer.valueOf(parsedInt), Integer.valueOf(max) });
			    }
			    return parsedInt;
			  }
			  
	
	@Override
	public void onCommand(String command, String[] args) throws Exception {
	    if (args.length > 1)
	    {
	      Item item = Item.getItemById(0);
	      try
	      {
	        item = getItemByText(args[1]);
	      }
	      catch (Exception exception)
	      {
	        Parallaxa.addChatMessage("m8 I couldn't find anything like that.. try again with an ID or somein' fam.");
	        return;
	      }
	      int amount = 1;
	      int damage = 0;
	      if (args.length > 2)
	      {
	        Integer newInt = Integer.valueOf(Integer.parseInt(args[2]));
	        if (newInt.intValue() <= 0)
	        {
	          Parallaxa.addChatMessage("The stack size you provided is too small. Using 1.");
	        }
	        else if (newInt.intValue() > 64)
	        {
	          amount = 64;
	          Parallaxa.addChatMessage("The stack size you provided is too big. Using 64.");
	        }
	        else
	        {
	          try
	          {
	            amount = parseIntBounded(args[2], 1, 64);
	          }
	          catch (NumberInvalidException e)
	          {
	            e.printStackTrace();
	          }
	        }
	      }
	      if (args.length > 3) {
	        try
	        {
	          damage = parseInt(args[3]);
	        }
	        catch (NumberInvalidException e)
	        {
	          e.printStackTrace();
	        }
	      }
	      ItemStack itemStack = new ItemStack(item, amount, damage);
	      if (args.length > 4)
	      {
	        String jsonData = command
	          .substring(args[0].length() + args[1].length() + args[2].length() + args[3].length() + 4);
	        try
	        {
	          NBTBase base = JsonToNBT.func_180713_a(jsonData);
	          if (!(base instanceof NBTTagCompound))
	          {
	            Parallaxa.addChatMessage("Invalid NBT/JSON data given.");
	            return;
	          }
	          itemStack.setTagCompound((NBTTagCompound)base);
	        }
	        catch (NBTException exception)
	        {
	          Parallaxa.addChatMessage("Invalid NBT/JSON data given.");
	          return;
	        }
	      }
	      Wrapper.getPlayer().inventory.addItemStackToInventory(itemStack);
	      Inventory.updateInventory();
	      Parallaxa.addChatMessage("Item \"" + itemStack.getDisplayName() + "\" is now in your inventory.");
	    }
	    else
	    {
	      Parallaxa.addChatMessage("provide me an id or somein' fam.");
	    }
	  }
	}


