package nexus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import nexus.commands.DrillToggleCommand;
import nexus.features.AutoTrophyFish;
import nexus.features.StopFuelUpdateDrill;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

@Mod(modid = "Nexus", version = "1.0.0")
public class Nexus {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static boolean devMode = false;
	public static HashMap<String, KeyBinding> keyBindings = new HashMap<>();
	public static boolean nameChangeToggle = true;
	
	public static final String prefix = "§0[§5Nex§dus§0]§r " ;
	
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		ClientCommandHandler.instance.registerCommand(new DrillToggleCommand());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(new StopFuelUpdateDrill());
		MinecraftForge.EVENT_BUS.register(new AutoTrophyFish());
		MinecraftForge.EVENT_BUS.register(this);
		
		registerKeybind("Trophy Fish", Keyboard.KEY_P);
		registerKeybind("Name Changer");
		for (KeyBinding keyBinding : keyBindings.values()){
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}
	
	@SubscribeEvent
	public void onKeyPress(InputEvent.KeyInputEvent event){
		if(keyBindings.get("Trophy Fish").isPressed()){
			AutoTrophyFish.toggleFishing();
		}
		if(keyBindings.get("Name Changer").isPressed()){
			Nexus.nameChangeToggle = !Nexus.nameChangeToggle;
			mc.thePlayer.addChatMessage(new ChatComponentText(Nexus.prefix + "§fName Changer: " + (Nexus.nameChangeToggle ? "§r§aEnabled" : "§r§4Disabled")));
		}
	}
	
	private void registerKeybind(String name, int... key){
		int b = key.length > Keyboard.KEY_NONE ? key[0] : Keyboard.KEY_NONE;
		keyBindings.put(name, new KeyBinding(name, b, "Nexus"));
	}
	
}
