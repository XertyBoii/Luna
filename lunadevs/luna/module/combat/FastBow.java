package lunadevs.luna.module.combat;

import lunadevs.luna.category.Category;
import lunadevs.luna.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBow extends Module{

	//Coded by faith.
	
	public FastBow() {
		super("FastBow", 0, Category.COMBAT, false);
	}

	public void onUpdate() {
		if(!this.isEnabled) return;
		
	    if ((mc.thePlayer.getHealth() > 0.0F) && 
	  	      ((mc.thePlayer.onGround) || (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)) && 
	  	      (mc.thePlayer.inventory.getCurrentItem() != null) && 
	  	      ((mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow)) && 
	  	      (mc.gameSettings.keyBindUseItem.pressed))
	  	    {
	  	      mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, 
	  	        mc.thePlayer.inventory.getCurrentItem());
	  	      mc.thePlayer.inventory
	  	        .getCurrentItem()
	  	        .getItem()
	  	        .onItemRightClick(mc.thePlayer.inventory.getCurrentItem(), 
	  	        mc.theWorld, mc.thePlayer);
	  	      for (int i = 0; i < 20; i++) {
	  	        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
	  	      }
	  	      Minecraft.getMinecraft().getNetHandler().addToSendQueue(
	  	        new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, 
	  	        new BlockPos(0, 0, 0), EnumFacing.DOWN));
	  	      mc.thePlayer.inventory
	  	        .getCurrentItem()
	  	        .getItem()
	  	        .onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), 
	  	        mc.theWorld, mc.thePlayer, 10);
	  	    }
	  	  }

		
	
	@Override
	public String getValue() {
		return null;
	}
	
	
	
}
