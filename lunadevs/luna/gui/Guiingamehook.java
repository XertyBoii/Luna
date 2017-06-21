package lunadevs.luna.gui;

import java.awt.Color;

import com.darkmagician6.eventapi.EventTarget;

import lunadevs.luna.category.Category;
import lunadevs.luna.main.Luna;
import lunadevs.luna.manage.ModuleManager;
import lunadevs.luna.manage.TabGuiManager;
import lunadevs.luna.module.Module;
import lunadevs.luna.utils.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;

public class Guiingamehook extends GuiIngame{
	
	public Guiingamehook(Minecraft mcIn) {
		super(mcIn);
	}
	 public void func_175180_a(float p_175180_1_){
		 super.func_175180_a(p_175180_1_);
		 this.renderClientName();
		 this.renderClientVersion();
		 this.renderCords();
		 this.renderFPS();
		 this.renderMods();
		 TabGuiManager.init();
		 TabGuiManager.render();
	 }
	 
     public void renderClientName(){
         Luna.fontRenderer50.drawStringWithShadow(Luna.CLIENT_NAME, 3, -1, 0xFF9931FF);
     }
  
  public void renderClientVersion(){
         Luna.fontRenderer.drawStringWithShadow("b"+Luna.CLIENT_BUILD, 55, 6, 0xFFFFFFFF);
  }
     public void renderCords(){
         Luna.fontRenderer.drawStringWithShadow("XYZ: " + String.valueOf(Math.round(Luna.mc.thePlayer.posX) + " " +  Math.round(Luna.mc.thePlayer.posY) + " " + Math.round(Luna.mc.thePlayer.posZ) + " "), 5, GuiMainMenu.height-10, 0x7200ff);
     }
     public void renderFPS(){
         Luna.fontRenderer.drawStringWithShadow("FPS: " + String.valueOf(Luna.mc.debugFPS), 5, GuiMainMenu.height-18, 0x7200ff);
     }
		public void renderMods(){
			int y = 18;
			for (Module m : Luna.moduleManager.getModules()) {
				final boolean setTransition = false;
				if (m.getTransition() > 0) {
					m.setTransition(m.getTransition() - 1);
				}
				if (!m.isEnabled) continue;
				if (m.value == true){
				Luna.fontRenderer.drawStringWithShadow(m.name + "\2477[" + m.getValue() + "]", ((Luna.mc.displayWidth / 2 + 15) - (Luna.fontRenderer).getStringWidth(m.getName() + m.getValue())) - 21 + m.getTransition(), y - 15, 0xFF7200ff);
				}else if (m.value == false){
				Luna.fontRenderer.drawStringWithShadow(m.name, ((Luna.mc.displayWidth / 2 + 15) - (Luna.fontRenderer).getStringWidth(m.getName()))  - 19 +  m.getTransition(), y - 15, 0xFF7200ff);
				}
					y += Luna.fontRenderer.getStringHeight(m.getName() + m.getValue());
				}
			ModuleManager.mods.sort(new Comparator());
			}
	
	 public static Color fade(long offset, float fade)
	  {
	    float hue = (float)(System.nanoTime() + offset) / 1.0E10F % 1.0F;
	    long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()), 16);
	    Color c = new Color((int)color);
	    return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade, c.getAlpha() / 255.0F);
	  }
}
