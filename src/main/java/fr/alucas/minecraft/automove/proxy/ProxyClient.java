package fr.alucas.minecraft.automove.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import fr.alucas.minecraft.automove.event.HandlerMouseEvent;
import fr.alucas.minecraft.automove.event.HandlerRenderEvent;

public class ProxyClient extends ProxyCommon {
	@Override
	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(new HandlerMouseEvent());
		MinecraftForge.EVENT_BUS.register(new HandlerRenderEvent());
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
