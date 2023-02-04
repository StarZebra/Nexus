package nexus.mixin;

import net.minecraft.client.gui.FontRenderer;
import nexus.Nexus;
import nexus.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
	
	protected MixinFontRenderer() {
	}
	
	@Shadow
	protected abstract void renderStringAtPos(String paramString, boolean paramBoolean);
	
	@Shadow
	public abstract int getStringWidth(String paramString);
	
	@Inject(method = {"renderStringAtPos"}, at = {@At("HEAD")}, cancellable = true)
	private void renderString(String text, boolean shadow, CallbackInfo ci) {
		if (Utils.inSkyblock() && text != null && text.contains(Nexus.mc.getSession().getUsername())) {
			ci.cancel();
			renderStringAtPos(text.replaceAll(Nexus.mc.getSession().getUsername(), "§zJerry"), shadow);
		}
	}
	
	@Inject(method = {"getStringWidth"}, at = {@At("RETURN")}, cancellable = true)
	private void getStringWidth(String text, CallbackInfoReturnable<Integer> cir) {
		if (Utils.inSkyblock() && text != null && text.contains(Nexus.mc.getSession().getUsername())) {
			cir.setReturnValue(getStringWidth(text.replaceAll(Nexus.mc.getSession().getUsername(), "§zJerry")));
		}
	}
}
