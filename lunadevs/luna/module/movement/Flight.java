package lunadevs.luna.module.movement;

import org.lwjgl.input.Keyboard;

import lunadevs.luna.category.Category;
import lunadevs.luna.main.Luna;
import lunadevs.luna.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

public class Flight extends Module{

	public Flight() {
		super("Flight", Keyboard.KEY_G, Category.MOVEMENT, true);
	}
	@Override
	public void onUpdate() {
		if (!this.isEnabled) return;
		Minecraft.thePlayer.onGround = true;
		Minecraft.thePlayer.capabilities.isFlying = true;
		super.onUpdate();
	}
	
	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		Minecraft.thePlayer.onGround = false;
		Timer.timerSpeed = 1.0F;
		Minecraft.thePlayer.capabilities.isFlying = false;
		super.onDisable();
	}
	
	@Override
	public String getValue() {
		return "Vanilla";
	}

}
