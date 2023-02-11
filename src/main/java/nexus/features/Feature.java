package nexus.features;

import gg.essential.universal.UChat;
import net.minecraft.client.Minecraft;
import nexus.Nexus;

abstract class Feature {
	protected static Minecraft mc = Minecraft.getMinecraft();
	
	protected static void printdev(String text){
		if(Nexus.devMode){
			System.out.println("[Nexus Developer Mode]: "+text);
			UChat.chat("[Nexus Developer Mode]: " +text);
		}
	}
}
