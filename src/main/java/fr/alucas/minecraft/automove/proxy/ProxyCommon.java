package fr.alucas.minecraft.automove.proxy;

import net.minecraft.entity.player.EntityPlayer;

public class ProxyCommon {
	/**
	 * Register the FML/Forge events.
	 */
	public void registerEvents() {
	}

	/**
	 * Return the played entity.
	 * @return The played entity. Null if not initialized yet.
	 */
	public EntityPlayer getPlayer() {
		return null;
	}
}
