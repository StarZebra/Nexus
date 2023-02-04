package nexus.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import nexus.Nexus;
import nexus.features.StopFuelUpdateDrill;

public class DrillToggleCommand extends CommandBase {
	
	@Override
	public String getCommandName() {
		return "drill";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getCommandName();
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		StopFuelUpdateDrill.toggled = !StopFuelUpdateDrill.toggled;
		Nexus.mc.thePlayer.addChatMessage(new ChatComponentText("§0§l[§3§lDrill§0§l]§r " + (StopFuelUpdateDrill.toggled ? "§r§aEnabled" : "§r§4Disabled") + " module."));
	}
	
	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}
}
