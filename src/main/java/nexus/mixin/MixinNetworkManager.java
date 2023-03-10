package nexus.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import nexus.events.ReceivePacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
	@Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
		if (MinecraftForge.EVENT_BUS.post(new ReceivePacketEvent(packet))) ci.cancel();
	}
	
	@Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("RETURN"), cancellable = true)
	private void readReturn(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
		if (MinecraftForge.EVENT_BUS.post(new ReceivePacketEvent.Post(packet))) ci.cancel();
	}
}