package nexus;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nexus.commands.DrillToggleCommand;
import nexus.features.StopFuelUpdateDrill;

@Mod(modid = "Nexus", version = "1.0.0")
public class Nexus {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		ClientCommandHandler.instance.registerCommand(new DrillToggleCommand());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(new StopFuelUpdateDrill());
	}
	
}
