package nexus.utils;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nexus.Nexus;
import org.jetbrains.annotations.NotNull;

public final class PlayerRotationUtil {
	@NotNull
	private final PlayerRotation.Rotation startRot;
	@NotNull
	private final PlayerRotation.Rotation endRot;
	private final long startTime;
	private final long endTime;
	private volatile boolean done;
	
	public PlayerRotationUtil(@NotNull PlayerRotation.RotationData rotationData){
		this.startRot = rotationData.getStartRot();
		this.endRot = rotationData.getEndRot();
		startTime = rotationData.getStartTime();
		endTime = rotationData.getEndTime();
		this.done = rotationData.getDone();
	}
	
	@SubscribeEvent
	public void renderWorldLast(@NotNull RenderWorldLastEvent event){
		if (Nexus.mc.thePlayer == null || Nexus.mc.theWorld == null) {
			MinecraftForge.EVENT_BUS.unregister(this);
			return;
		}
		if (System.currentTimeMillis() <= this.endTime) {
			Nexus.mc.thePlayer.rotationYaw = interpolate(this.startRot.getYaw(), this.endRot.getYaw());
			Nexus.mc.thePlayer.rotationPitch = interpolate(this.startRot.getPitch(), this.endRot.getPitch());
		} else if (!done) {
			Nexus.mc.thePlayer.rotationYaw = this.endRot.getYaw();
			Nexus.mc.thePlayer.rotationPitch = this.endRot.getPitch();
			this.done = true;
			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
	
	private float interpolate(float start, float end) {
		float spentMillis = (System.currentTimeMillis() - this.startTime);
		float relativeProgress = spentMillis / (this.endTime - this.startTime);
		return (end - start) * easeOutCubic(relativeProgress) + start;
	}
	
	private float easeOutCubic(double number) {
		return (float)(1.0D - Math.pow(1.0D - number, 3.0D));
	}
}
