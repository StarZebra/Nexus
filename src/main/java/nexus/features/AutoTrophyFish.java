package nexus.features;

import gg.essential.api.utils.Multithreading;
import javafx.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.*;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nexus.Nexus;
import nexus.events.ReceivePacketEvent;
import nexus.utils.PlayerRotation;
import nexus.utils.Utils;

public class AutoTrophyFish extends Feature{
	
	private static MovingObjectPosition raytrace = null;
	public static boolean toggled = false;
	private static long rotateCooldown = 0L;
	private static EntityFishHook playersFishHook = null;
	
	
	public static void toggleFishing(){
		if(!toggled){
			raytrace = mc.thePlayer.rayTrace(100.0, 1.0f);
			int rodSlot = getFishingRod();
			if(rodSlot == -1){
				mc.thePlayer.addChatMessage(new ChatComponentText(Nexus.prefix +"§cRod not detected in hotbar."));
				return;
			}
			mc.thePlayer.inventory.currentItem = rodSlot;
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
			rotateCooldown = System.currentTimeMillis();
		} else{
			raytrace = null;
		}
		
		toggled = !toggled;
		mc.thePlayer.addChatMessage(new ChatComponentText(Nexus.prefix + "§fTrophy Fish: " + (AutoTrophyFish.toggled ? "§r§aEnabled" : "§r§4Disabled")));
		
		
	}
	
	@SubscribeEvent
	public void onPacketReceive(ReceivePacketEvent event){
		if(!toggled) return;
		if(event.packet instanceof S14PacketEntity.S17PacketEntityLookMove){
			Entity entity = ((S14PacketEntity.S17PacketEntityLookMove) event.packet).getEntity(mc.theWorld);
			if(!(entity instanceof EntityFishHook) || ((EntityFishHook) entity).angler != mc.thePlayer) return;
			playersFishHook = (EntityFishHook) entity;
		}
		if(event.packet instanceof S2APacketParticles){
			if(playersFishHook == null) return;
			if(((S2APacketParticles) event.packet).getParticleType() != EnumParticleTypes.WATER_WAKE && ((S2APacketParticles) event.packet).getParticleType() != EnumParticleTypes.FLAME) return;
			if(((S2APacketParticles) event.packet).getParticleCount() != 6 || ((S2APacketParticles) event.packet).getParticleSpeed() != 0.2f) return;
			double particlePosX = ((S2APacketParticles) event.packet).getXCoordinate();
			double particlePosZ = ((S2APacketParticles) event.packet).getZCoordinate();
			if(playersFishHook.getDistance(particlePosX, playersFishHook.posY, particlePosZ) < 0.1){
				printdev(String.format("Count: %s Speed: %s Distance: %s",
						((S2APacketParticles) event.packet).getParticleCount(),
						((S2APacketParticles) event.packet).getParticleSpeed(),
						playersFishHook.getDistance(particlePosX, playersFishHook.posY, particlePosZ)));
				reelIn(true);
			}
		}
	}
	
	private static int getFishingRod(){
		for (int i = 0; i <= 7; i++){
			ItemStack item = mc.thePlayer.inventory.mainInventory[i];
			if(item == null) continue;
			if(item.getItem() == Items.fishing_rod){
				return i;
			}
		}
		return -1;
	}
	
	private static void reelIn(Boolean recast){
		playersFishHook = null;
		Multithreading.runAsync(
				() -> {
					long randomCooldown = (long) ((Math.random() * (100 - 50)) + 50);
					printdev(String.format("%s", randomCooldown));
					try {
						Thread.sleep(randomCooldown);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
					if(!recast) return;
					randomCooldown = (long) ((Math.random() * ((275 + 25) - (275 - 25))) + 275 - 25);
					printdev(String.format("%s", randomCooldown));
					try {
						Thread.sleep(randomCooldown);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
				}
		);
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event){
		if(!toggled) return;
		if(mc.thePlayer.inventory.currentItem != getFishingRod()){
			mc.thePlayer.addChatMessage(new ChatComponentText(Nexus.prefix +"§cDetected slot change, disabling."));
			toggled = false;
			playersFishHook = null;
			return;
		}
		if(playersFishHook != null){
			if(playersFishHook.onGround && playersFishHook.motionX == 0.0 && playersFishHook.motionZ == 0.0){
				printdev("Should recast");
				reelIn(true);
			}
		}
		if(System.currentTimeMillis() - rotateCooldown >= 10000){
			printdev("Rotating");
			printdev(String.format("Coords: %s", raytrace.getBlockPos().getX()));
			float yawOffset = (float)(Math.random() * 5.0D + -2.5D);
			float pitchOffset = (float)(Math.random() * 5.0D + -2.5D);
			Pair<Float, Float> yawAndPitch = Utils.blockPosToYawPitch(
					new BlockPos(raytrace.getBlockPos().getX()+1, raytrace.getBlockPos().getY(), raytrace.getBlockPos().getZ()),
					mc.thePlayer.getPositionVector());
			printdev(String.format("OffsetY: %s OffsetX: %s", yawOffset, pitchOffset));
			new PlayerRotation(
					new PlayerRotation.Rotation(yawAndPitch.getKey()+pitchOffset, yawAndPitch.getValue()+yawOffset),
			600L);
			rotateCooldown = System.currentTimeMillis();
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event){
		playersFishHook = null;
		rotateCooldown = 0L;
	}
}
