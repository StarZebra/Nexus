package nexus.features;

import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nexus.Nexus;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SessionLogin {
	
	public Session session = null;
	
	@SubscribeEvent
	public void postGuiScreen(GuiScreenEvent.InitGuiEvent.Post post){
		if(Nexus.devMode && post.gui instanceof GuiMainMenu){
			post.buttonList.add(new GuiButton(1803, 5, 5, 100, 20, "Login"));
			post.buttonList.add(new GuiButton(1804, 115, 5, 100, 20, "Save"));
		}
	}
	
	@SubscribeEvent
	public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post post){
		if(post.gui instanceof GuiMainMenu){
			if(post.button.id == 1803){
				if(this.session == null){
					this.session = Minecraft.getMinecraft().getSession();
				}
				String str = JOptionPane.showInputDialog("Login");
				if((str == null || str.isEmpty())) return;
				if(str.equalsIgnoreCase("reset")) {
					try{
						Field field = Minecraft.class.getDeclaredField("session");
						field.setAccessible(true);
						field.set(Minecraft.getMinecraft(), this.session);
					} catch (Exception e){
						//L exception
						e.printStackTrace();
					}
					return;
				}
				try {
					Field field = Minecraft.class.getDeclaredField("session");
					field.setAccessible(true);
					String string = new JsonParser().parse(
							new InputStreamReader(
									new URL(String.valueOf(
											new StringBuilder()
													.append("https://sessionserver.mojang.com/session/minecraft/profile/")
													.append(str.split(": ")[1])))
											.openStream())).getAsJsonObject().get("name").getAsString();
					field.set(Minecraft.getMinecraft(), new Session(string, str.split(": ")[1], str.split(": ")[0], "mojang"));
					
				}catch (Exception e){
					//L exception
					e.printStackTrace();
				}
			}
			
			/*
			Code below is for saving your own session id (used for testing)
			 */
			if(post.button.id == 1804){
				try {
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get("savedData"))));
					writer.write(String.valueOf(new StringBuilder()
							.append(Minecraft.getMinecraft().getSession().getToken()).append(": ").append(Minecraft.getMinecraft().getSession().getPlayerID())));
					writer.close();
				}catch (Exception e){
					//L exception
					e.printStackTrace();
				}
			}
		}
	}
}
