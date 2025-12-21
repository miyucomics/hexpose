package miyucomics.hexpose.mixin;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.common.casting.actions.rw.OpTheCoolerRead;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexpose.iotas.item_stack.ItemStackIota;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(OpTheCoolerRead.class)
public abstract class OpTheCoolerReadMixin implements ConstMediaAction {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true, remap = false)
    private void readFromItemStack(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
        if (args.get(0) instanceof ItemStackIota stack) {
            ADIotaHolder holder = IXplatAbstractions.INSTANCE.findDataHolder(stack.getStack());
            if (holder == null)
                return;
            Iota iota = holder.readIota(env.getWorld());
            cir.setReturnValue(List.of(Objects.requireNonNullElseGet(iota, NullIota::new)));
        }
    }
}