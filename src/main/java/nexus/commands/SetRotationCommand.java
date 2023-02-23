package nexus.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import nexus.Nexus;

public class SetRotationCommand extends CommandBase{
	
	private static boolean setPitch = false;
	
	@Override
	public String getCommandName() {
		return "setrot";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getCommandName();
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0){
			Nexus.mc.thePlayer.addChatMessage(new ChatComponentText(Nexus.prefix + "§c/setrot <yaw> [pitch]"));
			return;
		}
		String yaw = args[0];
		String pitch = "";
		if(args.length > 1){
			pitch = args[1];
			if(pitch.matches("[a-z]+")) return;
			setPitch = true;
		}
		
		if(yaw.matches("[a-z]+")) return;
		
		Nexus.mc.thePlayer.rotationYaw = Float.parseFloat(yaw);
		if(pitch.length() > 0){
			Nexus.mc.thePlayer.rotationPitch = Float.parseFloat(pitch);
		}
		Nexus.mc.thePlayer.addChatMessage(new ChatComponentText(Nexus.prefix + "Yaw: " + yaw + (setPitch ? " Pitch: " + pitch : "")));
		setPitch = false;
	}
	
	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}
}


