package nexus.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import nexus.Nexus;

public class SetYawCommand extends CommandBase{
	@Override
	public String getCommandName() {
		return "setyaw";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getCommandName();
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0){
			String argYaw = args[0];
			if(argYaw.matches("[a-z]+")) return;
			
			Nexus.mc.thePlayer.rotationYaw = Float.parseFloat(argYaw);
			Nexus.mc.thePlayer.addChatMessage(new ChatComponentText(Nexus.prefix + "Set yaw to " + argYaw));
			
		}
	}
	
	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}
}


