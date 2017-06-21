package lunadevs.luna.module.player;


import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;

import lunadevs.luna.category.Category;
import lunadevs.luna.events.EventMotion;
import lunadevs.luna.events.EventType;
import lunadevs.luna.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoPot extends Module{
	
	int ticks = 0;
	int health = 10;
	boolean doPot = false;

	public AutoPot() {
		super("AutoPot", 0, Category.PLAYER, false);
	}

	@EventTarget(Priority.HIGHEST)
	public void heal(EventMotion event){
		if (this.ticks > 0)
	    {
	      this.ticks -= 1;
	      return;
	    }
	    if (event.getType() == EventType.PRE)
	    {
	      ItemThing potSlot = getHealingItemFromInventory();
	      if ((this.ticks == 0) && (mc.thePlayer.getHealth() <= health && (potSlot.getSlot() != -1)))
	      {
	          event.getLocation().setPitch(123);
	          event.setCancelled(true);
	          mc.gameSettings.keyBindJump.pressed = true;
	          mc.gameSettings.keyBindJump.pressed = false;
	        this.doPot = true;
	      }
	    }
	    else if (this.doPot)
	    {
	      ItemThing potSlot = getHealingItemFromInventory();
	      if (potSlot.getSlot() == -1) {
	        return;
	      }
	      if (this.doPot)
	      {
	        if (potSlot.getSlot() < 9)
	        {
	          Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(potSlot.getSlot()));
	          Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
	          Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
	        }
	        else
	        {
	          swap(potSlot.getSlot(), 5);
	          Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(5));
	          Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
	          if (potSlot.isSoup()) {
	            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
	          }
	          Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
	        }
	        this.ticks =  10;
	        this.doPot = false;
	      }
	    }
	  }
	  
	  public int getCount()
	  {
	    int pot = -1;
	    int counter = 0;
	    for (int i = 0; i < 36; i++) {
	      if (Minecraft.thePlayer.inventory.mainInventory[i] != null)
	      {
	        ItemStack is = Minecraft.thePlayer.inventory.mainInventory[i];
	        Item item = is.getItem();
	        if ((item instanceof ItemPotion))
	        {
	          ItemPotion potion = (ItemPotion)item;
	          if (potion.getEffects(is) != null) {
	            for (Object o : potion.getEffects(is))
	            {
	              PotionEffect effect = (PotionEffect)o;
	              if ((effect.getPotionID() == Potion.heal.id) && (ItemPotion.isSplash(is.getItemDamage()))) {
	                counter++;
	              }
	            }
	          }
	        }
	        if ((item instanceof ItemSoup)) {
	          counter++;
	        }
	      }
	    }
	    return counter;
	  }
	  
	  private ItemThing getHealingItemFromInventory()
	  {
	    int itemSlot = -1;
	    int counter = 0;
	    boolean soup = false;
	    for (int i = 0; i < 36; i++) {
	      if (Minecraft.thePlayer.inventory.mainInventory[i] != null)
	      {
	        ItemStack is = Minecraft.thePlayer.inventory.mainInventory[i];
	        Item item = is.getItem();
	        if ((item instanceof ItemPotion))
	        {
	          ItemPotion potion = (ItemPotion)item;
	          if (potion.getEffects(is) != null) {
	            for (Object o : potion.getEffects(is))
	            {
	              PotionEffect effect = (PotionEffect)o;
	              if ((effect.getPotionID() == Potion.heal.id) && (ItemPotion.isSplash(is.getItemDamage())))
	              {
	                counter++;
	                itemSlot = i;
	                soup = false;
	              }
	            }
	          }
	        }
	      }
	    }
	    return new ItemThing(itemSlot, soup);
	  }
	  
	  private void swap(int slot, int hotbarSlot)
	  {
	    mc.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, hotbarSlot, 2, Minecraft.thePlayer);
	  }
	  
	  public class ItemThing
	  {
	    private int slot;
	    private boolean soup;
	    
	    public ItemThing(int slot, boolean soup)
	    {
	      this.slot = slot;
	      this.soup = soup;
	    }
	    
	    public int getSlot()
	    {
	      return this.slot;
	    }
	    
	    public boolean isSoup()
	    {
	      return this.soup;
	    }
	  }
	  private int findPotion(int startSlot, int endSlot)
	  {
	    for (int i = startSlot; i < endSlot; i++)
	    {
	      ItemStack stack = 
	        mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	      if ((stack != null) && (stack.getItem() == Items.potionitem) && 
	        (ItemPotion.isSplash(stack.getItemDamage()))) {
	        for (Object o : ((ItemPotion)stack.getItem()).getEffects(stack)) {
	          if (((PotionEffect)o).getPotionID() == Potion.heal.id) {
	            return i;
	          }
	        }
	      }
	    }
	    return -1;
	  }
	
	@Override
	public String getValue() {
		return null;
	}
}
