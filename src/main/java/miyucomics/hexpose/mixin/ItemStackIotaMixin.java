package miyucomics.hexpose.mixin;

import at.petrak.hexcasting.api.casting.iota.Iota;
import com.samsthenerd.inline.api.data.ItemInlineData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ram.talia.moreiotas.api.casting.iota.ItemStackIota;

@Mixin(targets = "ram.talia.moreiotas.api.casting.iota.ItemStackIota$1")
public abstract class ItemStackIotaMixin {
	@Shadow
	public abstract Iota deserialize(NbtElement par1, ServerWorld par2) throws IllegalArgumentException;

	@Inject(method = "display", at = @At("HEAD"), cancellable = true)
	private void onDisplay(NbtElement tag, CallbackInfoReturnable<Text> cir) {
		ItemStack stack = ((ItemStackIota) deserialize(tag, null)).getItemStack();
		if (stack.isEmpty()) {
			cir.setReturnValue(Text.translatable("hexpose.item_stack.null").formatted(Formatting.GRAY));
			return;
		}

		cir.setReturnValue(stack.getName().copy()
			.append(" (")
			.append(Text.literal(String.valueOf(stack.getCount())).formatted(stack.getRarity().formatting))
			.append("): ")
			.append(ItemInlineData.make(stack)));
	}
}