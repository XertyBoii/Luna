package lunadevs.luna.module.fun;

import org.lwjgl.input.Keyboard;

import lunadevs.luna.category.Category;
import lunadevs.luna.main.Parallaxa;
import lunadevs.luna.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

//coded by faith

public class SpamKill extends Module{

	public SpamKill() {
		super("SpamKill", Keyboard.KEY_NONE, Category.FUN, false);
	}
	
	public void onUpdate() {
		if(!this.isEnabled) return;
		for(int kek = 0; kek < 500; kek++) {
			Parallaxa.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Parallaxa.getPlayer().posX, Parallaxa.getPlayer().posY - 0.05D, Parallaxa.getPlayer().posZ, false));
			Parallaxa.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Parallaxa.getPlayer().posX, Parallaxa.getPlayer().posY, Parallaxa.getPlayer().posZ, false));
		}
	}
	
	public String getValue() {
		return null;
	}
	
}
