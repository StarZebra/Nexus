package nexus.mixin;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import nexus.features.StopFuelUpdateDrill;
import nexus.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerController {

	@Redirect(method = {"isHittingPosition"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areItemStackTagsEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
	private boolean shouldTagsBeEqual(ItemStack stackA, ItemStack stackB){
		if(!StopFuelUpdateDrill.toggled) return ItemStack.areItemStackTagsEqual(stackA, stackB);
		
		return Utils.getSkyblockItemID(Utils.getExtraAttributes(stackA)).equals(Utils.getSkyblockItemID(Utils.getExtraAttributes(stackB)));
		
	}
}
