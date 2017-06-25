package lunadevs.luna.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lunadevs.luna.events.EventRender3D;
import lunadevs.luna.events.EventRenderNameTag;
import lunadevs.luna.friend.FriendManager;
import lunadevs.luna.main.Luna;
import lunadevs.luna.manage.ModuleManager;
import lunadevs.luna.mcleaks.ChatColor;
import lunadevs.luna.utils.MathUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import org.lwjgl.opengl.GL11;

import lunadevs.luna.category.Category;
import lunadevs.luna.module.Module;
import lunadevs.luna.utils.MathUtil;
import lunadevs.luna.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import parallaxadevs.Utils.GLUtil;

public class OldNameTags extends Module{

    public static boolean active;
    private double distance;
    private double scale;
    private boolean armor = true;
    private Character formatChar = new Character('\247');
	public boolean formatting = true;
    public static Map<EntityLivingBase, double[]> entityPositions;

	public OldNameTags() {
		super("2DNameTags", 0, Category.RENDER, false);
	}
	
	@Override
	public void onRender() {
		if(!this.isEnabled) return;
		for (Object o : this.mc.theWorld.playerEntities) {
		      EntityPlayer p = (EntityPlayer)o;
		      if ((p != this.mc.func_175606_aa()) && (p.isEntityAlive())) {
		        double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosX;
		        double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosY;
		        double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosZ;
		        
		        renderNameTag(p, this.formatting  ==true ? p.getDisplayName().getFormattedText() : p.getName(), pX, pY, pZ);
		      }
		    }
		  }
		  
		  public void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
		    FontRenderer var12 = this.mc.fontRendererObj;
		    pY += (entity.isSneaking() ? 0.5D : 0.7D);
		    float var13 = this.mc.thePlayer.getDistanceToEntity(entity) / 4.0F;
		    if (var13 < 1.6F) {
		      var13 = 1.6F;
		    }
		    int colour = 16777215;
		    if (!this.formatting ==true) {
		      tag = ChatColor.stripColor(tag);
		    }
		    
		    if (FriendManager.isFriend(entity.getName()))
		    {
		      colour = 5488374;
		    } else if (entity.isInvisible()) {
		      colour = 16756480;
		    } else if (entity.isSneaking()) {
		      colour = 11468800;
		    }
		    tag = entity.getName();
		    
		    double health = Math.ceil(entity.getHealth() + entity.getAbsorptionAmount()) / 2.0D;
		    ChatColor healthCol; ChatColor healthCol1; if (health < 5.0D) {
		      healthCol = ChatColor.RED; } else { ChatColor healthCol2;
		      if ((health > 5.0D) && (health < 6.5D)) {
		        healthCol = ChatColor.YELLOW;
		      } else
		        healthCol = ChatColor.GREEN;
		    }
		    if (Math.floor(health) == health) {
		      tag = tag + " " + healthCol + (int)Math.floor(health);
		    } else {
		      tag = tag + " " + healthCol + health;
		    }
		    RenderManager renderManager = this.mc.getRenderManager();
		    int color = 16776960;
		    float scale = var13 * 2.0F;
		    scale /= 100.0F;
		    GL11.glPushMatrix();
		    GL11.glTranslatef((float)pX, (float)pY + 1.4F, (float)pZ);
		    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		    GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		    GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		    GL11.glScalef(-scale, -scale, scale);
		    GLUtil.setGLCap(2896, false);
		    GLUtil.setGLCap(2929, false);
		    Tessellator var14 = Tessellator.getInstance();
		    net.minecraft.client.renderer.WorldRenderer var15 = var14.getWorldRenderer();
		    int width = this.mc.fontRendererObj.getStringWidth(tag) / 2;
		    GLUtil.setGLCap(3042, true);
		    GL11.glBlendFunc(770, 771);
		    
		    lunadevs.luna.utils.RenderHelper.drawBorderedRect(-width - 2, -(this.mc.fontRendererObj.FONT_HEIGHT + 1), width + 2, 2.0F, 1.0F, -16777216, 1593835520);
		    var12.func_175065_a(tag, -width, -(this.mc.fontRendererObj.FONT_HEIGHT - 1), colour, true);
		    GL11.glPushMatrix();
		    if (this.armor == true) {
		      int xOffset = 0;
		      for (ItemStack armourStack : entity.inventory.armorInventory) {
		        if (armourStack != null)
		          xOffset -= 8;
		      }
		      Object renderStack;
		      if (entity.getHeldItem() != null) {
		        xOffset -= 8;
		        renderStack = entity.getHeldItem().copy();
		        if ((((ItemStack)renderStack).hasEffect()) && (((((ItemStack)renderStack).getItem() instanceof ItemTool)) || ((((ItemStack)renderStack).getItem() instanceof ItemArmor))))
		          ((ItemStack)renderStack).stackSize = 1;
		        renderItemStack((ItemStack)renderStack, xOffset, -26);
		        xOffset += 16;
		      }
		      for (ItemStack armourStack : entity.inventory.armorInventory)
		        if (armourStack != null)
		        {

		          ItemStack renderStack1 = armourStack.copy();
		          if ((renderStack1.hasEffect()) && (((renderStack1.getItem() instanceof ItemTool)) || ((renderStack1.getItem() instanceof ItemArmor))))
		            renderStack1.stackSize = 1;
		          renderItemStack(renderStack1, xOffset, -26);
		          xOffset += 16;
		        }
		    }
		    GL11.glPopMatrix();
		    GLUtil.revertAllCaps();
		    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		    GL11.glPopMatrix();
		  }
		  
		  public void renderItemStack(ItemStack stack, int x, int y) { GL11.glPushMatrix();
		    GL11.glDepthMask(true);
		    GlStateManager.clear(256);
		    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
		    this.mc.getRenderItem().zLevel = -150.0F;
		    whatTheFuckOpenGLThisFixesItemGlint();
		    this.mc.getRenderItem().func_180450_b(stack, x, y);
		    this.mc.getRenderItem().func_175030_a(this.mc.fontRendererObj, stack, x, y);
		    this.mc.getRenderItem().zLevel = 0.0F;
		    net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		    GlStateManager.disableCull();
		    GlStateManager.enableAlpha();
		    GlStateManager.disableBlend();
		    GlStateManager.disableLighting();
		    GlStateManager.scale(0.5D, 0.5D, 0.5D);
		    GlStateManager.disableDepth();
		    renderEnchantText(stack, x, y);
		    GlStateManager.enableDepth();
		    GlStateManager.scale(2.0F, 2.0F, 2.0F);
		    GL11.glPopMatrix();
		  }
		  
		  public void renderEnchantText(ItemStack stack, int x, int y) { int encY = y - 24;
		    if ((stack.getItem() instanceof ItemArmor)) {
		      int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
		      int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
		      int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
		      if (pLevel > 0) {
		        this.mc.fontRendererObj.drawString("p" + pLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		      if (tLevel > 0) {
		        this.mc.fontRendererObj.drawString("t" + tLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		      if (uLevel > 0) {
		        this.mc.fontRendererObj.drawString("u" + uLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		    }
		    if ((stack.getItem() instanceof net.minecraft.item.ItemBow)) {
		      int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
		      int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
		      int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
		      int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
		      if (sLevel > 0) {
		        this.mc.fontRendererObj.drawString("d" + sLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		      if (kLevel > 0) {
		        this.mc.fontRendererObj.drawString("k" + kLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		      if (fLevel > 0) {
		        this.mc.fontRendererObj.drawString("f" + fLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		      if (uLevel > 0) {
		        this.mc.fontRendererObj.drawString("u" + uLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		    }
		    if ((stack.getItem() instanceof net.minecraft.item.ItemSword)) {
		      int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
		      int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
		      int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
		      int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
		      if (sLevel > 0) {
		        this.mc.fontRendererObj.drawString("s" + sLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		      if (kLevel > 0) {
		        this.mc.fontRendererObj.drawString("k" + kLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		      if (fLevel > 0) {
		        this.mc.fontRendererObj.drawString("f" + fLevel, x * 2, encY, 16777215);
		        encY += 7;
		      }
		      if (uLevel > 0) {
		        this.mc.fontRendererObj.drawString("u" + uLevel, x * 2, encY, 16777215);
		      }
		    }
		  }
		  
		  public void whatTheFuckOpenGLThisFixesItemGlint() {
		    GlStateManager.disableLighting();
		    GlStateManager.disableDepth();
		    GlStateManager.disableBlend();
		    GlStateManager.enableLighting();
		    GlStateManager.enableDepth();
		    GlStateManager.disableLighting();
		    GlStateManager.disableDepth();
		    GlStateManager.func_179090_x();
		    GlStateManager.disableAlpha();
		    GlStateManager.disableBlend();
		    GlStateManager.enableBlend();
		    GlStateManager.enableAlpha();
		    GlStateManager.func_179098_w();
		    GlStateManager.enableLighting();
		    GlStateManager.enableDepth();
		  }
		  


	@Override
	public void onEnable() {
        EventRenderNameTag.cancel=true;
		super.onEnable();
	}

	@Override
	public void onDisable() {
        EventRenderNameTag.cancel=false;
		super.onDisable();
	}

	@Override
	public String getValue() {
		return null;
	}

}