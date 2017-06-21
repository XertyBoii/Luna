package lunadevs.luna.utils;

import java.awt.Color;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class RenderHelper {
	private static final ScaledResolution sr;

	static {
		sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
	}

	public static final ScaledResolution getScaledRes() {
		final ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		return scaledRes;
	}

	public static void drawHollowRect(final float posX, final float posY, final float posX2, final float posY2,
			final float width, final int color, final boolean center) {
		final float corners = width / 2.0f;
		final float side = width / 2.0f;
		if (center) {
			drawRect(posX - side, posY - corners, posX + side, posY2 + corners, color);
			drawRect(posX2 - side, posY - corners, posX2 + side, posY2 + corners, color);
			drawRect(posX - corners, posY - side, posX2 + corners, posY + side, color);
			drawRect(posX - corners, posY2 - side, posX2 + corners, posY2 + side, color);
		} else {
			drawRect(posX - width, posY - corners, posX, posY2 + corners, color);
			drawRect(posX2, posY - corners, posX2 + width, posY2 + corners, color);
			drawRect(posX - corners, posY - width, posX2 + corners, posY, color);
			drawRect(posX - corners, posY2, posX2 + corners, posY2 + width, color);
		}
	}

	public static double interpolate(final double now, final double then) {
		return then + (now - then) * Minecraft.getMinecraft().timer.renderPartialTicks;
	}

	public static double[] interpolate(final Entity entity) {
		final double posX = interpolate(entity.posX, entity.lastTickPosX) - RenderManager.renderPosX;
		final double posY = interpolate(entity.posY, entity.lastTickPosY) - RenderManager.renderPosY;
		final double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - RenderManager.renderPosZ;
		return new double[] { posX, posY, posZ };
	}

	public static int createShader(final String shaderCode, final int shaderType) throws Exception {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
			if (shader == 0) {
				return 0;
			}
			ARBShaderObjects.glShaderSourceARB(shader, (CharSequence) shaderCode);
			ARBShaderObjects.glCompileShaderARB(shader);
			if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			}
			return shader;
		} catch (Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}
	}

	public static String getLogInfo(final int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
	}

	public static void drawGradientBorderedRect(final float posX, final float posY, final float posX2,
			final float posY2, final float width, final int color, final int startColor, final int endColor,
			final boolean center) {
		drawGradientRect(posX, posY, posX2, posY2, startColor, endColor);
		drawHollowRect(posX, posY, posX2, posY2, width, color, center);
	}

	public static void drawCoolLines(final AxisAlignedBB mask) {
		GL11.glPushMatrix();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1,
			final int col1, final int col2) {
		drawRect(x, y, x2, y2, col2);
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawBorderedCorneredRect(final float x, final float y, final float x2, final float y2,
			final float lineWidth, final int lineColor, final int bgColor) {
		drawRect(x, y, x2, y2, bgColor);
		final float f = (lineColor >> 24 & 0xFF) / 255.0f;
		final float f2 = (lineColor >> 16 & 0xFF) / 255.0f;
		final float f3 = (lineColor >> 8 & 0xFF) / 255.0f;
		final float f4 = (lineColor & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glEnable(3553);
		drawRect(x - 1.0f, y, x2 + 1.0f, y - lineWidth, lineColor);
		drawRect(x, y, x - lineWidth, y2, lineColor);
		drawRect(x - 1.0f, y2, x2 + 1.0f, y2 + lineWidth, lineColor);
		drawRect(x2, y, x2 + lineWidth, y2, lineColor);
		GL11.glDisable(3553);
		GL11.glDisable(3042);
	}

	public static double interp(final double from, final double to, final double pct) {
		return from + (to - from) * pct;
	}

	public static double interpPlayerX() {
		return interp(Minecraft.getMinecraft().thePlayer.lastTickPosX, Minecraft.getMinecraft().thePlayer.posX,
				Minecraft.getMinecraft().timer.renderPartialTicks);
	}

	public static double interpPlayerY() {
		return interp(Minecraft.getMinecraft().thePlayer.lastTickPosY, Minecraft.getMinecraft().thePlayer.posY,
				Minecraft.getMinecraft().timer.renderPartialTicks);
	}

	public static double interpPlayerZ() {
		return interp(Minecraft.getMinecraft().thePlayer.lastTickPosZ, Minecraft.getMinecraft().thePlayer.posZ,
				Minecraft.getMinecraft().timer.renderPartialTicks);
	}

	public static void drawFilledBox(final AxisAlignedBB mask) {
		final WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
		final Tessellator tessellator = Tessellator.instance;
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		tessellator.draw();
	}

	public static void glColor(final Color color) {
		GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
				color.getAlpha() / 255.0f);
	}

	public static void glColor(final int hex) {
		final float alpha = (hex >> 24 & 0xFF) / 255.0f;
		final float red = (hex >> 16 & 0xFF) / 255.0f;
		final float green = (hex >> 8 & 0xFF) / 255.0f;
		final float blue = (hex & 0xFF) / 255.0f;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void drawGradientRect(final float x, final float y, final float x1, final float y1,
			final int topColor, final int bottomColor) {
		GL11.glEnable(1536);
		GL11.glShadeModel(7425);
		GL11.glBegin(7);
		glColor(topColor);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		glColor(bottomColor);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glShadeModel(7424);
		GL11.glDisable(1536);
	}

	public static void drawLines(final AxisAlignedBB mask) {
		GL11.glPushMatrix();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	public static void drawOutlinedBoundingBox(final AxisAlignedBB mask) {
		final WorldRenderer var2 = Tessellator.instance.getWorldRenderer();
		final Tessellator var3 = Tessellator.instance;
		var2.startDrawing(3);
		var2.addVertex(mask.minX, mask.minY, mask.minZ);
		var2.addVertex(mask.maxX, mask.minY, mask.minZ);
		var2.addVertex(mask.maxX, mask.minY, mask.maxZ);
		var2.addVertex(mask.minX, mask.minY, mask.maxZ);
		var2.addVertex(mask.minX, mask.minY, mask.minZ);
		var3.draw();
		var2.startDrawing(3);
		var2.addVertex(mask.minX, mask.maxY, mask.minZ);
		var2.addVertex(mask.maxX, mask.maxY, mask.minZ);
		var2.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		var2.addVertex(mask.minX, mask.maxY, mask.maxZ);
		var2.addVertex(mask.minX, mask.maxY, mask.minZ);
		var3.draw();
		var2.startDrawing(1);
		var2.addVertex(mask.minX, mask.minY, mask.minZ);
		var2.addVertex(mask.minX, mask.maxY, mask.minZ);
		var2.addVertex(mask.maxX, mask.minY, mask.minZ);
		var2.addVertex(mask.maxX, mask.maxY, mask.minZ);
		var2.addVertex(mask.maxX, mask.minY, mask.maxZ);
		var2.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		var2.addVertex(mask.minX, mask.minY, mask.maxZ);
		var2.addVertex(mask.minX, mask.maxY, mask.maxZ);
		var3.draw();
	}

	public static void drawRect(final float g, final float h, final float i, final float j, final int col1) {
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(7);
		GL11.glVertex2d((double) i, (double) h);
		GL11.glVertex2d((double) g, (double) h);
		GL11.glVertex2d((double) g, (double) j);
		GL11.glVertex2d((double) i, (double) j);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawRectNoBlend(final float g, final float h, final float i, final float j, final int col1) {
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(7);
		GL11.glVertex2d((double) i, (double) h);
		GL11.glVertex2d((double) g, (double) h);
		GL11.glVertex2d((double) g, (double) j);
		GL11.glVertex2d((double) i, (double) j);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(2848);
	}

	public static void renderThroughWalls(boolean bool) {
		if (bool) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(true);
			GlStateManager.enableBlend();
			GlStateManager.func_179090_x();
		} else {
			GL11.glDisable(GL11.GL_CULL_FACE);

			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);

			GlStateManager.func_179098_w();
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
		}
	}

}
