package lunadevs.luna.module.fun;

import org.lwjgl.input.Keyboard;

import lunadevs.luna.category.Category;
import lunadevs.luna.module.Module;
import net.minecraft.client.Minecraft;

//coded by faith

public class Twerk extends Module{

	private int faithisthebest;
	
	public Twerk() {
		super("Twerk", Keyboard.KEY_NONE, Category.FUN, false);
	}

	public void onUpdate() {
		if(!this.isEnabled) return;
	    this.faithisthebest += 1;
	    if (this.faithisthebest >= 2)
	    {
	      Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = 
	        (!Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed);
	      this.faithisthebest = 0;
	    }
	}
	
	@Override
	public String getValue() {
		return null;
	}
	
}
