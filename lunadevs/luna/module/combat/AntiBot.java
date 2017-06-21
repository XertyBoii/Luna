package lunadevs.luna.module.combat;

import com.darkmagician6.eventapi.EventTarget;

import lunadevs.luna.category.Category;
import lunadevs.luna.events.EventPacket;
import lunadevs.luna.module.Module;
import lunadevs.luna.option.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public class AntiBot extends Module {

	public static boolean active;
	@Option.Op(name = "Watchdog")
	public static boolean Watchdog = false;
	@Option.Op(name = "G.W.E.N")
	public static boolean GWEN = true;

	public AntiBot() {
		super("AntiBot", 0, Category.COMBAT, true);
	}

	public static String modname;

	@Override
	public void onUpdate() {
		if (!this.isEnabled)
			return;
		if (this.Watchdog == true) {
			if (this.GWEN == true) {
				this.GWEN = false;
			}
			modname = "Watchdog";
		} else if (this.GWEN == true) {
			if (this.Watchdog == true) {
				this.Watchdog = false;
			}
			modname = "G.W.E.N";
		}
		if (this.Watchdog == true) {
			watchdog();
		}
		super.onUpdate();
	}

	public void watchdog() {
		if (this.Watchdog == true) {
			for (Object entity : mc.theWorld.loadedEntityList) {
				if ((((Entity) entity).isInvisible()) && (entity != Minecraft.thePlayer)) {
					mc.theWorld.removeEntity((Entity) entity);
					((Entity) entity).setInvisible(false);
				}
			}
		}
	}

	@EventTarget
	public void receivePackets(EventPacket e) {
		if (!this.isEnabled)
			return;
		if (this.GWEN == true) {
			for (Object entity : mc.theWorld.loadedEntityList) {
				if ((e.getPacket() instanceof S0CPacketSpawnPlayer)) {
					S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) e.getPacket();
					double posX = packet.func_148942_f() / 32.0D;
					double posY = packet.func_148949_g() / 32.0D;
					double posZ = packet.func_148946_h() / 32.0D;

					double difX = z.player().posX - posX;
					double difY = z.player().posY - posY;
					double difZ = z.player().posZ - posZ;

					double dist = Math.sqrt(difX * difX + difY * difY + difZ * difZ);
					if ((dist <= 17.0D) && (posX != z.player().posX) && (posY != z.player().posY)
							&& (posZ != z.player().posZ)) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		active = false;
	}

	@Override
	public void onEnable() {
		active = true;
		super.onEnable();
	}

	@Override
	public String getValue() {
		return modname;
	}

}
