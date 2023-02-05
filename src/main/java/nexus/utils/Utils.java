package nexus.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import nexus.Nexus;

public class Utils {
	
	public static String removeFormatting(String string) {
		return string.replaceAll("§.", "");
	}
	
	public static boolean inSkyblock(){
		if(Nexus.mc.theWorld == null || Nexus.mc.thePlayer == null) return false;
		if(Nexus.mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1) == null) return false;
		return removeFormatting(Nexus.mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName()).contains("SKYBLOCK");
	}
	
	public static NBTTagCompound getExtraAttributes(ItemStack item) {
		if (item == null) {
			return null;
		}
		if (!item.hasTagCompound()) {
			return null;
		}
		
		return item.getSubCompound("ExtraAttributes", false);
	}
	
	public static String getSkyblockItemID(NBTTagCompound extraAttributes) {
		if (extraAttributes == null) {
			return null;
		}
		
		String itemId = extraAttributes.getString("id");
		if (itemId.equals("")) {
			return null;
		}
		
		return itemId;
	}
}
