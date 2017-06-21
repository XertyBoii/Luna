package lunadevs.luna.module.render;

import org.jibble.pircbot.Colors;
import org.lwjgl.opengl.GL11;

import com.zCore.Core.zCore;

import java.text.DecimalFormat;
import java.util.Iterator;

import lunadevs.luna.category.Category;
import lunadevs.luna.events.EventRender3D;
import lunadevs.luna.friend.FriendManager;
import lunadevs.luna.module.Module;
import lunadevs.luna.utils.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class CircleESP extends Module{

	public CircleESP() {
		super("CircleESP", 0, Category.RENDER, true);
	}
	
	
	public void onRender(EventRender3D e) {
		if(!this.isEnabled) return;
		for (final Object i : mc.theWorld.loadedEntityList) {
			final Entity entity;
			final Entity player = entity = (Entity) i;
			final Minecraft mc3 = mc;
			if (entity != Minecraft.thePlayer && player instanceof EntityPlayer && player != null
					&& !entity.isInvisible()) {
				final String name = player.getName();
				final Minecraft mc4 = mc;
				if (name == Minecraft.thePlayer.getName()) {
					continue;
				}
				final float posX = (float) ((float) player.lastTickPosX
						+ (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks);
				final float posY = (float) ((float) player.lastTickPosY
						+ (player.posY - player.lastTickPosY) * mc.timer.renderPartialTicks);
				final float posZ = (float) ((float) player.lastTickPosZ
						+ (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks);
				final Minecraft mc5 = mc;
				final float distance = Minecraft.thePlayer.getDistanceToEntity(player);
				final float health = ((EntityLivingBase) player).getHealth();
				final Minecraft mc6 = mc;
				final DecimalFormat decimal = new DecimalFormat("#.#");
				final float percent = Float.valueOf(decimal.format(health / 2.0f));
				if (percent >= 6.0f) {
					this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY,
							posZ - RenderManager.renderPosZ, 0.1f, 1.0f, 0.1f,
							1.0F);
				}
				if (percent < 6.0f) {
					this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY,
							posZ - RenderManager.renderPosZ, 1.0f, 0.5f, 0.0f,
							1.0F);
				}
				if (percent >= 3.0f) {
					continue;
				}
				this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY,
						posZ - RenderManager.renderPosZ, 1.0f, 0.0f, 0.0f, 1.0F);
			}

	}
}

public void draw2D(final Entity e, final double posX, final double posY, final double posZ, final float alpha,
		final float red, final float green, final float blue) {
	GlStateManager.pushMatrix();
	GlStateManager.translate(posX, posY, posZ);
	GL11.glNormal3f(0.0f, 0.0f, 0.0f);
	GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
	GlStateManager.scale(-0.1, -0.1, 0.1);
	GL11.glDisable(2896);
	GL11.glDisable(2929);
	GL11.glEnable(3042);
	GL11.glBlendFunc(770, 771);
	GlStateManager.func_179098_w();
	GlStateManager.depthMask(true);
		zCore.circleOutline(0.0, -8.199999809265137, 10.0, zCore.getRainbow(6000, -15 * 4));
		{
		if (!FriendManager.isFriend(e.getName())) {
			zCore.circleOutline(0.0, -8.199999809265137, 10.0, -1);
		} else {
			zCore.circleOutline(0.0, -8.199999809265137, 10.0, -13697046);
		}
	}
	GL11.glDisable(3042);
	GL11.glEnable(2929);
	GL11.glEnable(2896);
	GlStateManager.popMatrix();
}

public static void circleOutline(double x, double y, double radius, int color) {
	float red = (color >> 24 & 0xFF) / 255.0f;
	float green = (color >> 16 & 0xFF) / 255.0f;
	float blue = (color >> 8 & 0xFF) / 255.0f;
	float alpha = (color & 0xFF) / 255.0f;
	Tessellator tessellator = Tessellator.getInstance();
	GlStateManager.pushMatrix();
	GlStateManager.func_179090_x();
	GlStateManager.enableBlend();
	GlStateManager.color(green, blue, alpha, red);
	GL11.glEnable(2848);
	GL11.glEnable(2881);
	GL11.glHint(3154, 4354);
	GL11.glHint(3155, 4354);
	GL11.glBegin(3);
	for (int i = 0; i <= 360; ++i) {
		final double x2 = Math.sin(i * 3.141592653589793 / 180.0) * radius;
		final double y2 = Math.cos(i * 3.141592653589793 / 180.0) * radius;
		GL11.glVertex2d(x + x2, y + y2);
	}
	GL11.glEnd();
	GL11.glDisable(2848);
	GL11.glDisable(2881);
	GlStateManager.func_179098_w();
	GlStateManager.disableBlend();
	GlStateManager.popMatrix();
}

	
	public String getValue() {
		return "Rainbow";
	}
	
}
