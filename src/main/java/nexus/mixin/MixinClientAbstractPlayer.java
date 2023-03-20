package nexus.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import nexus.Nexus;
import org.apache.commons.codec.digest.DigestUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinClientAbstractPlayer {
	private static ResourceLocation getCape(String uuid){
		return (ResourceLocation) Nexus.capes.get(DigestUtils.sha256Hex(uuid));
	}
	
	@Inject(method = {"getLocationCape"}, at = {@At("RETURN")}, cancellable = true)
	public void getLocationCape(CallbackInfoReturnable<ResourceLocation> cir){
		ResourceLocation minecons = getCape(((AbstractClientPlayer)Nexus.mc.thePlayer).getUniqueID().toString());
		if(minecons != null){
			cir.setReturnValue(minecons);
		}
	}
}
