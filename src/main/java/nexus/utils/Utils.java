package nexus.utils;

import javafx.util.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
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
	
	public static Pair<Float, Float> blockPosToYawPitch(BlockPos blockPos, Vec3 playerPos){
		double diffX = blockPos.getX() - playerPos.xCoord - 0.5;
		double diffY = blockPos.getY() - (playerPos.yCoord +Nexus.mc.thePlayer.getEyeHeight()) + 0.5;
		double diffZ = blockPos.getZ() - playerPos.zCoord + 0.5;
		float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
		float dist = (float) Math.sqrt(diffY*diffX+diffZ*diffZ);
		float pitch = (float) (-(Math.toDegrees(Math.atan2(diffY, dist))));
		return new Pair<>(Nexus.mc.thePlayer.rotationYaw + wrapAngleTo180(yaw-Nexus.mc.thePlayer.rotationYaw),
				Nexus.mc.thePlayer.rotationPitch + wrapAngleTo180(pitch-Nexus.mc.thePlayer.rotationPitch));
	}
	
	private static float wrapAngleTo180(float angle) {
		return (float) (angle - Math.floor(angle / 360 + 0.5) * 360);
	}
	
}
