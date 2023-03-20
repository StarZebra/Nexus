package nexus;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import nexus.commands.DrillToggleCommand;
import nexus.commands.SetRotationCommand;
import nexus.features.AutoTrophyFish;
import nexus.features.SessionLogin;
import nexus.features.StopFuelUpdateDrill;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

@Mod(modid = "nexus", name = "Nexus", version = "1.0.2")
public class Nexus {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static boolean devMode = false;
	public static HashMap<String, KeyBinding> keyBindings = new HashMap<>();
	public static HashMap<String, ResourceLocation> capes = new HashMap<>();
	public static HashMap<File, ResourceLocation> capesLoaded = new HashMap<>();
	public static boolean nameChangeToggle = true;
	
	public static final String prefix = "§0[§5Nex§dus§0]§r " ;
	
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		new File(new File(mc.mcDataDir, "config"), "Nexus").mkdir();
		new File(mc.mcDataDir.getPath()+"/config/Nexus/capes").mkdir();
		ClientCommandHandler.instance.registerCommand(new DrillToggleCommand());
		ClientCommandHandler.instance.registerCommand(new SetRotationCommand());
		
		
		if(new File("NexusDev").exists()){
			devMode = true;
		}else{
			new File("NexusDev").createNewFile();
		}
		
		if(devMode){
			MinecraftForge.EVENT_BUS.register(new SessionLogin());
		}
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		Display.setTitle("New Fortnite Roblox Update");
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new StopFuelUpdateDrill());
		MinecraftForge.EVENT_BUS.register(new AutoTrophyFish());
		
		
		registerKeybind("Trophy Fish", Keyboard.KEY_P);
		registerKeybind("Name Changer");
		for (KeyBinding keyBinding : keyBindings.values()){
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}
	
	@Mod.EventHandler
	public void onPost(FMLPostInitializationEvent event) {
		loadCapes();
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
	
	private static void loadCapes() {
		try{
			HashMap<String, String> capeData = (HashMap<String, String>)(new Gson()).fromJson(new InputStreamReader((new URL("https://raw.githubusercontent.com/StarZebra/Sessions/main/capes.txt")).openStream()), HashMap.class);
			capes.clear();
			if(capeData != null){
				HashMap<String, ResourceLocation> cache = new HashMap<>();
				capeData.forEach((key, value) -> {
					try {
						ResourceLocation capeFromCache = (ResourceLocation) cache.get(value);
						if(capeFromCache == null){
							System.out.println("cape from cache is null");
							ResourceLocation cape;
							File capeFile = new File(mc.mcDataDir.getPath()+ "/config/Nexus/capes/"+ value + ".png");
							if(!capeFile.exists()) return;
							if(capesLoaded.containsKey(capeFile)){
								cape = capesLoaded.get(capeFile);
							}else{
								cape = mc.getTextureManager().getDynamicTextureLocation("nexus", new DynamicTexture(ImageIO.read(capeFile)));
								capesLoaded.put(capeFile, cape);
							}
							cache.put(value, cape);
							capes.put(key, cape);
							
						}else{
							capes.put(key, cache.get(value));
						}
					}catch (Exception e){
						e.printStackTrace();
						System.out.println("Error loading cape " + value);
					}
				});
			}
			if(devMode){
				System.out.println((new Gson()).toJson(capeData));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	

	
}
