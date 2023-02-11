package nexus.features;

import gg.essential.api.utils.Multithreading;
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

public class AutoTrophyFish extends Feature{
	
	public static boolean toggled = false;
	
	private static EntityFishHook playersFishHook = null;
	
	
	public static void toggleFishing(){
		if(!toggled){
			int rodSlot = getFishingRod();
			if(rodSlot == -1){
				mc.thePlayer.addChatMessage(new ChatComponentText(Nexus.prefix +"§cRod not detected in hotbar."));
				return;
			}
			mc.thePlayer.inventory.currentItem = rodSlot;
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
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
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event){
		playersFishHook = null;
	}
}
