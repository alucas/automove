package fr.alucas.minecraft.automove.client.input;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInputFromOptions;

public class MovementInputAuto extends MovementInputFromOptions {
	private GameSettings gameSettings;
	private ChunkCoordinates destination;
	private EntityPlayer player;

	public MovementInputAuto(GameSettings gameSettings, EntityPlayer player) {
		super(gameSettings);

		this.gameSettings = gameSettings;
		this.player = player;
	}

	@Override
	public void updatePlayerMoveState() {
		if (destination == null) {
			super.updatePlayerMoveState();

			return;
		}
		
		moveForward = 1;
		sneak = false;
		jump = false;

		if (((int) player.boundingBox.minX == destination.posX || (int) player.boundingBox.maxX == destination.posX) && ((int) player.boundingBox.minZ == destination.posZ || (int) player.boundingBox.maxZ == destination.posZ)) {
			if (player.motionX == 0 && player.motionZ == 0) {
				destination = null;
			}
			
			// Artificially release the sprint key
			KeyBinding.setKeyBindState(gameSettings.keyBindSprint.getKeyCode(), false);
						
			sneak = true;
			moveForward = -1;
			
			MotionValues motionValues = getNextMotionValues();
			System.out.println(motionValues.motionX + ", " + player.motionX + ", " + motionValues.motionZ + ", " + player.motionZ);
			if (motionValues.motionX * player.motionX < 0 || motionValues.motionZ * player.motionZ < 0) {
				moveForward = 0;
			}

			return;
		}
		
		player.rotationYaw = getBestYaw();
		
		// Artificially press the sprint key
		KeyBinding.setKeyBindState(gameSettings.keyBindSprint.getKeyCode(), true);
		KeyBinding.onTick(gameSettings.keyBindSprint.getKeyCode());
		
		// Next position prediction
//
//		double nextPosMinX = player.boundingBox.minX + nextMotionX;
//		double nextPosMaxX = player.boundingBox.maxX + nextMotionX;
//		double nextPosMinZ = player.boundingBox.minZ + nextMotionZ;
//		double nextPosMaxZ = player.boundingBox.maxZ + nextMotionZ;
		
		double posY = player.posY - player.yOffset;

		if (player.onGround) {
			jump = true;
			jump &= player.worldObj.isAirBlock((int) (player.boundingBox.minX), (int) (posY - 0.2D), (int) (player.boundingBox.minZ));
			jump &= player.worldObj.isAirBlock((int) (player.boundingBox.minX), (int) (posY - 0.2D), (int) (player.boundingBox.maxZ));
			jump &= player.worldObj.isAirBlock((int) (player.boundingBox.maxX), (int) (posY - 0.2D), (int) (player.boundingBox.maxZ));
			jump &= player.worldObj.isAirBlock((int) (player.boundingBox.maxX), (int) (posY - 0.2D), (int) (player.boundingBox.minZ));
		} else {
			jump = true;
		}
	}

	private float getBestYaw() {
		int posX = MathHelper.floor_double(player.posX);
		int posZ = MathHelper.floor_double(player.posZ);
		
		double distance = MathHelper.sqrt_double(posX - destination.posX) + MathHelper.sqrt_double(posZ - destination.posZ);

		double offsetX = 0.5D;
		double offsetZ = 0.5D;
		
		if (distance < 12) {
		} else if (posX <  destination.posX) {
			offsetX = 0.0D;
		} else if (posX > destination.posX) {
			offsetX = 1.0D;
		}

		if (distance < 12) {
		} else if (posZ >  destination.posZ) {
			offsetZ = 1.0D;
		} else if (posZ < destination.posZ) {
			offsetZ = 0.0D;
		}

		return (float) ((Math.atan2(destination.posZ + offsetZ - player.posZ, destination.posX + offsetX - player.posX) / Math.PI * 180.0D) - 90.0D);
	}

	public void setDestination(ChunkCoordinates destination) {
		this.destination = destination;
	}
	
	private MotionValues getNextMotionValues() {
		MotionValues motionValues = new MotionValues();
		motionValues.motionX = player.motionX;
		motionValues.motionZ = player.motionZ;

		float f2 = 0.91F;

		if (player.onGround)
		{
			f2 = player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.boundingBox.minY) - 1, MathHelper.floor_double(player.posZ)).slipperiness * 0.91F;
		}

		float f3 = 0.16277136F / (f2 * f2 * f2);

		float jumpFactor;

		if (player.onGround) {
			jumpFactor = player.getAIMoveSpeed() * f3;
		} else {
			jumpFactor = player.jumpMovementFactor;
		}

		double motion = moveForward * 0.98F * jumpFactor;
		motionValues.motionX += -motion * MathHelper.sin(player.rotationYaw * (float)Math.PI / 180.0F);
		motionValues.motionZ += motion * MathHelper.cos(player.rotationYaw * (float)Math.PI / 180.0F);
		
		return motionValues;
	}
	
	private class MotionValues {
		public double motionX;
		public double motionZ;
	}
}
