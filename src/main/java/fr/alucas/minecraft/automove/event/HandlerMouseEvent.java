package fr.alucas.minecraft.automove.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.MouseEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.alucas.minecraft.automove.Automove;
import fr.alucas.minecraft.automove.client.input.MovementInputAuto;

public class HandlerMouseEvent {
	@SubscribeEvent
	public void onMouseEvent(MouseEvent event) {
		// Press right button.
		if (event.button != 1 || event.buttonstate != true) {
			return;
		}

		EntityPlayer player = Automove.proxy.getPlayer();
		if (player == null) {
			return;
		}

		ItemStack equippedStack = player.getCurrentEquippedItem();
		if (equippedStack != null && equippedStack.getItem() != null) {
			return;
		}

		if (!(player instanceof EntityClientPlayerMP)) {
			return;
		}

		EntityClientPlayerMP playerClient = (EntityClientPlayerMP) player;

		if (!(playerClient.movementInput instanceof MovementInputAuto)) {
			playerClient.movementInput = new MovementInputAuto(Minecraft.getMinecraft().gameSettings, player);
		}

		MovingObjectPosition targetPosition = player.rayTrace(10, 1.0F);

		((MovementInputAuto) playerClient.movementInput).setDestination(new ChunkCoordinates(targetPosition.blockX, targetPosition.blockY, targetPosition.blockZ));
	}
}
