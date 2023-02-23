package nexus.features;

import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nexus.Nexus;
import nexus.events.ReceivePacketEvent;
import nexus.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class StopFuelUpdateDrill {
	
	double worldLoad = System.currentTimeMillis();
	public static boolean toggled = false;
	boolean thing = false;

	@SubscribeEvent
	public void receivePacket(ReceivePacketEvent event){
		if(!Utils.inSkyblock()) return;
		if(!toggled) return;
		
		if(!thing){
			double currTime = System.currentTimeMillis();
			if(currTime-worldLoad <= 3000) return;
			thing = true;
		}
		
		if(Nexus.mc.currentScreen != null) return;
		if(event.packet instanceof S2FPacketSetSlot) {
			if(((S2FPacketSetSlot) event.packet).func_149174_e() == null) return;
			String currentItem = Utils.getSkyblockItemID(Utils.getExtraAttributes(Nexus.mc.thePlayer.getHeldItem()));
			List<String> allowedItems = Arrays.asList("GEMSTONE_GAUNTLET", "TITANIUM_DRILL_4");
			
			if(currentItem != null && allowedItems.contains(currentItem)){
				event.setCanceled(true);
			}
		}
		
		/*if(event.packet instanceof S30PacketWindowItems){
			if(((S30PacketWindowItems) event.packet).func_148911_c() != 0) return;
			event.setCanceled(true);
		}*/
	}
	
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event){
		worldLoad = System.currentTimeMillis();
		toggled = false;
		thing = false;
	}
}

