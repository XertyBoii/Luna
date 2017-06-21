package lunadevs.luna.module.render;

import org.jibble.pircbot.Colors;
import org.lwjgl.opengl.GL11;

import com.zCore.Core.zCore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lunadevs.luna.category.Category;
import lunadevs.luna.events.EventRender3D;
import lunadevs.luna.events.Render3DEvent;
import lunadevs.luna.friend.FriendManager;
import lunadevs.luna.main.Parallaxa;
import lunadevs.luna.manage.ModuleManager;
import lunadevs.luna.module.Module;
import lunadevs.luna.utils.EntityUtils;
import lunadevs.luna.utils.MathUtils;
import lunadevs.luna.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class CircleESP extends Module{

	public float partialTicks;
	public static boolean active;
    private double scale;
    private boolean armor;
    private Character formatChar = new Character('\247');
    public static Map<EntityLivingBase, double[]> entityPositions;
	
	public CircleESP() {
		super("CircleESP", 0, Category.RENDER, true);
	}
	
	private double distance = 100;
    private HashMap<Integer, Integer> colors = new HashMap<>();
    @Override
	public void onRender() {
		if(!this.isEnabled) return;
		ModuleManager.findMod(OldNameTags.class).setEnabled(true);
		this.distance = 10.0D;
        this.scale = 0.1D;
        this.armor = true;
        GlStateManager.pushMatrix();
        for (Object o : mc.theWorld.loadedEntityList)
        {
            Entity ent = (Entity)o;
            if (ent != mc.thePlayer)
            {
                if (((ent instanceof EntityPlayer)) && (!ent.isInvisible()))
                {
                    double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * EventRender3D.partialTicks - RenderManager.renderPosX;
                    double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * EventRender3D.partialTicks - RenderManager.renderPosY + ent.height + 0.5D;
                    double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * EventRender3D.partialTicks - RenderManager.renderPosZ;
                    String str = ent.getDisplayName().getFormattedText();
                    if (FriendManager.isFriend(ent.getName())) {
                        str = str.replace(ent.getName(), "\247b" + ent.getName());
                    }
                    String colorString = this.formatChar.toString();
                    double health = MathUtils.round(((EntityPlayer)ent).getHealth(), 2);
                    if (health >= 12.0D) {
                        colorString = String.valueOf(colorString) + "2";
                    } else if (health >= 4.0D) {
                        colorString = String.valueOf(colorString) + "6";
                    } else {
                        colorString = String.valueOf(colorString) + "4";
                    }
                    str = String.valueOf(str) + " " + colorString + Math.round(health / 2.0D);
                    float dist = mc.thePlayer.getDistanceToEntity(ent);
                    float scale = 0.02672F;
                    float factor = (float)(dist <= this.distance ? this.distance * this.scale : dist * this.scale);
                    scale *= factor;
                    GlStateManager.pushMatrix();
                    GlStateManager.disableDepth();
                    GlStateManager.translate(posX, posY, posZ);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-mc.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(mc.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-scale, -scale, scale);
                    GlStateManager.disableLighting();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                    GlStateManager.func_179090_x();
                    worldRenderer.startDrawingQuads();
                    int stringWidth = Parallaxa.fr.getStringWidth(str) / 2;
                    GL11.glColor3f(0.0F, 0.0F, 0.0F);
                    GL11.glLineWidth(1.0E-6F);
                    GL11.glBegin(3);
                    GL11.glVertex2d(-stringWidth - 2, -0.8D);
                    GL11.glVertex2d(-stringWidth - 2, 8.8D);
                    GL11.glVertex2d(-stringWidth - 2, 8.8D);
                    GL11.glVertex2d(stringWidth + 2, 8.8D);
                    GL11.glVertex2d(stringWidth + 2, 8.8D);
                    GL11.glVertex2d(stringWidth + 2, -0.8D);
                    GL11.glVertex2d(stringWidth + 2, -0.8D);
                    GL11.glVertex2d(-stringWidth - 2, -0.8D);
                    GL11.glEnd();
                    worldRenderer.func_178974_a(0, 100);
                    worldRenderer.addVertex(-stringWidth - 2, -0.8D, 0.0D);
                    worldRenderer.addVertex(-stringWidth - 2, 8.8D, 0.0D);
                    worldRenderer.addVertex(stringWidth + 2, 8.8D, 0.0D);
                    worldRenderer.addVertex(stringWidth + 2, -0.8D, 0.0D);
                    tessellator.draw();
                    GlStateManager.func_179098_w();
                    Parallaxa.fr.drawString(str, -Parallaxa.fr.getStringWidth(str) / 2, 0, -1);
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    if ((this.armor) && ((ent instanceof EntityPlayer)))
                    {
                        List<ItemStack> itemsToRender = new ArrayList();
                        for (int i = 0; i < 5; i++)
                        {
                            ItemStack stack = ((EntityPlayer)ent).getEquipmentInSlot(i);
                            if (stack != null) {
                                itemsToRender.add(stack);
                            }
                        }
                        int x = -(itemsToRender.size() * 8);
                        Iterator<ItemStack> iterator2 = itemsToRender.iterator();
                        while (iterator2.hasNext())
                        {
                            ItemStack stack = (ItemStack)iterator2.next();
                            GlStateManager.disableDepth();
                            RenderHelper.enableGUIStandardItemLighting();
                            mc.getRenderItem().zLevel = -100.0F;
                            mc.getRenderItem().func_175042_a(stack, x, -18);
                            mc.getRenderItem().func_175030_a(Minecraft.getMinecraft().fontRendererObj, stack, x, -18);
                            mc.getRenderItem().zLevel = 0.0F;
                            RenderHelper.disableStandardItemLighting();
                            GlStateManager.enableDepth();
                            String text = "";
                            if (stack != null)
                            {
                                int y = 0;
                                int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
                                int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                                int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
                                if (sLevel > 0)
                                {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("s" + sLevel, x, y);
                                    y -= 9;
                                }
                                if (fLevel > 0)
                                {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("f" + fLevel, x, y);
                                    y -= 9;
                                }
                                if (kLevel > 0)
                                {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("k" + kLevel, x, y);
                                }
                                else if ((stack.getItem() instanceof ItemArmor))
                                {
                                    int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
                                    int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
                                    int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                                    if (pLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("p" + pLevel, x, y);
                                        y -= 9;
                                    }
                                    if (tLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("t" + tLevel, x, y);
                                        y -= 9;
                                    }
                                    if (uLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("u" + uLevel, x, y);
                                    }
                                }
                                else if ((stack.getItem() instanceof ItemBow))
                                {
                                    int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                                    int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                                    int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
                                    if (powLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("p" + powLevel, x, y);
                                        y -= 9;
                                    }
                                    if (punLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("p" + punLevel, x, y);
                                        y -= 9;
                                    }
                                    if (fireLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("f" + fireLevel, x, y);
                                    }
                                }
                                else if (stack.getRarity() == EnumRarity.EPIC)
                                {
                                    drawEnchantTag("\247cGod", x, y);
                                }
                                x += 16;
                            }
                        }
                    }
                    GlStateManager.popMatrix();
                }
                GlStateManager.disableBlend();
            }
        }
        GlStateManager.popMatrix();

   
		colors.put(10,0xFF3c8c48);
        colors.put(9, 0xFF508c3c);
        colors.put(8, 0xFF688c3c);
        colors.put(7, 0xFF7e8c3c);
        colors.put(6, 0xFF8c853c);
        colors.put(5, 0xFF8c7d3c);
        colors.put(4, 0xFFbc7838);
        colors.put(3, 0xFFbc5f38);
        colors.put(2, 0xFFc1372a);
        colors.put(1, 0xFFd62020);
        colors.put(0, 0xFFff0019);
		GlStateManager.pushMatrix();
        ArrayList<Entity> fk = (ArrayList<Entity>) z.mc().theWorld.loadedEntityList;
        fk.sort(((o1, o2) -> {
            double dist2 = o2.getDistanceToEntity(z.p());
            double dist = o1.getDistanceToEntity(z.p());
            if (dist > dist2) {
                return -1;
            }
            if (dist < dist2) {
                return 1;
            }
            return 0;
        }));
        for (Object ent1 : fk) {
            Entity ent = (Entity) ent1;
            if (z.p().getDistanceToEntity(ent) > 100F) {
                continue;
            }
            if (ent == mc.thePlayer) {
                continue;
            }
            if (ent instanceof EntityPlayer && !ent.isInvisible()) {
                final double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
                final double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY + ent.height + 0.5;
                final double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                String str = "";
                if (ent.isSneaking()) {
                    str = "\247c" + "";

                }
                final float dist = z.mc().thePlayer.getDistanceToEntity(ent);
                float scale = 0.03f;
                final float factor = (float) ((dist <= 8.0f) ? (8.0 * 0.1) : (dist * 0.1));
                scale *= factor;
                GlStateManager.pushMatrix();
                GlStateManager.disableDepth();
                GlStateManager.translate(posX, posY, posZ);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-z.mc().renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                GlStateManager.pushMatrix();
                GlStateManager.scale(-scale, -scale, scale);
                RenderUtils.disableGL3D();
                GlStateManager.disableAlpha();
                GlStateManager.clear(256);
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -1100000.0f);
                Minecraft.getMinecraft().fontRendererObj.drawString(str,-Minecraft.getMinecraft().fontRendererObj.getStringWidth(str) / 2, -10, -1);
                GL11.glDisable(32823);
                GL11.glPolygonOffset(1.0f, 1100000.0f);
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                RenderUtils.enableGL3D(0.5f);
                GlStateManager.popMatrix();
                GlStateManager.scale(-0.03f, -0.03f, 0.03f);
                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                int color = 0xFF3c8c48;
                try {
                    color = colors.get((int) ((EntityPlayer) ent).getHealth() / 2);
                } catch (Exception e1) {}
                RenderUtils.DrawArc(0,44,40,0,(((EntityPlayer) ent).getHealth() / 3.1),100, 6, color);
                RenderUtils.DrawArc(0,44,39.5f,0,6.3,100, 1, -1);


                GlStateManager.func_179098_w();
                GlStateManager.disableLighting();
                GlStateManager.popMatrix();

            }
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();

}
    
    @Override
    public void onEnable(){
    	z.addChatMessageP("§7Keep in mind that CircleESP might cause Render Bugs §7while §7its §7on.");
    }
    
    private static void drawEnchantTag(String text, int x, int y)
    {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x = (int)(x * 1.75D);
        GL11.glScalef(0.57F, 0.57F, 0.57F);
        y -= 4;
        Parallaxa.fontRendererGUI.drawString(text, x, -36 - y, 64250);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
	public String getValue() {
		return "Beta";
	}
	
}
