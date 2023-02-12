package nexus.utils;

import net.minecraftforge.common.MinecraftForge;
import nexus.Nexus;
import org.jetbrains.annotations.NotNull;

public final class PlayerRotation {
	
	public PlayerRotation(@NotNull Rotation rotation, long time) {
		@NotNull Rotation startRot;
		@NotNull Rotation endRot;
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis() + time;
		boolean done;
		done = false;
		startRot = new Rotation(Nexus.mc.thePlayer.rotationYaw, Nexus.mc.thePlayer.rotationPitch);
		endRot = rotation;
		MinecraftForge.EVENT_BUS.register(new PlayerRotationUtil(new RotationData(startRot, endRot, startTime, endTime, done)));
	}
	
	
	public static final class Rotation {
		private final float pitch;
		private final float yaw;
		
		public Rotation(float yaw, float pitch){
			this.pitch = pitch;
			this.yaw = yaw;
		}
		
		public float getPitch() {
			return this.pitch;
		}
		
		public float getYaw() {
			return this.yaw;
		}
	}
	
	public static final class RotationData {
		@NotNull
		private final Rotation startRot;
		@NotNull
		private final Rotation endRot;
		
		private final long startTime;
		private final long endTime;
		private final boolean done;
		
		public RotationData(@NotNull Rotation startRot, @NotNull Rotation endRot, long startTime, Long endTime, boolean done){
			this.endRot = endRot;
			this.startRot = startRot;
			this.startTime = startTime;
			this.endTime = endTime;
			this.done = done;
		}
		
		@NotNull
		public PlayerRotation.Rotation getStartRot() {
			return this.startRot;
		}
		
		public long getStartTime() {
			return this.startTime;
		}
		
		public long getEndTime() {
			return this.endTime;
		}
		
		@NotNull
		public PlayerRotation.Rotation getEndRot() {
			return this.endRot;
		}
		
		public boolean getDone() {
			return this.done;
		}
		
	}
	
}
