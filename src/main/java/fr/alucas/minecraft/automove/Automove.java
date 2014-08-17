package fr.alucas.minecraft.automove;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.alucas.minecraft.automove.proxy.ProxyCommon;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Automove {
	@Instance(value = Reference.MODID)
	public static Automove instance;

	@SidedProxy(clientSide = Reference.PROXY_CLIENT_PATH, serverSide = Reference.PROXY_SERVER_PATH)
	public static ProxyCommon proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.registerEvents();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}