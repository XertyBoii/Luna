package lunadevs.luna.module.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.zCore.Core.zCore;

import lunadevs.luna.category.Category;
import lunadevs.luna.events.EventPacket;
import lunadevs.luna.module.Module;
import lunadevs.luna.module.exploits.zPackets;
import lunadevs.luna.utils.faithsminiutils.Timer;
import lunadevs.luna.utils.faithsminiutils.Wrapper;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module{

	public Criticals() {
		super("Criticals", 0, Category.COMBAT, false);
	}

	private static Timer timer = new Timer();
	private static boolean cancelSomePackets;

	public static void doCrit()
	  {
		if(Timer.hasReach(500)) {
            zCore.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + 0.05, Wrapper.getPlayer().posZ, false));
            zCore.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ, false));
            Timer.reset();
            cancelSomePackets = true;
		}
}

	
	@EventTarget
	  public void onEntityAttack(EventPacket event)
	  {
	    if ((event.getType() == EventPacket.EventPacketType.SEND) && ((event.getPacket() instanceof C02PacketUseEntity)) && (mc.thePlayer.onGround) && 
	      (((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK)) {
	      doCrit();
	    }
	  }
	
	
	@Override
	public String getValue() {
		return null;
	}

}
