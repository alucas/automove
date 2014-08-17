package fr.alucas.minecraft.automove.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerRenderEvent {
	@SubscribeEvent
	public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		Minecraft minecraft = Minecraft.getMinecraft();

		EntityClientPlayerMP player = minecraft.thePlayer;
		WorldClient world = minecraft.theWorld;
		float partialTickTime = event.partialTicks;
		RenderBlocks renderBlocks = new RenderBlocks(world);

		MovingObjectPosition targetPosition = player.rayTrace(10, partialTickTime);
		if (targetPosition == null) {
			return;
		}

		double deltaX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTickTime;
		double deltaY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTickTime;
		double deltaZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTickTime;

		minecraft.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

		OpenGlHelper.glBlendFunc(774, 768, 1, 0);
		GL11.glColor4f(1.0F, 0.5F, 0.5F, 0.5F);
		GL11.glPushMatrix();
		GL11.glPolygonOffset(-5.0F, -5.0F);
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		Tessellator par1Tessellator = Tessellator.instance;
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setTranslation(-deltaX, -deltaY, -deltaZ);
		par1Tessellator.disableColor();

		renderBlocks.renderBlockByRenderType(world.getBlock(targetPosition.blockX, targetPosition.blockY, targetPosition.blockZ), targetPosition.blockX, targetPosition.blockY, targetPosition.blockZ);

		par1Tessellator.draw();
		par1Tessellator.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPolygonOffset(0.0F, 0.0F);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
	}
}
