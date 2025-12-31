package miyucomics.hexpose.mixin;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.common.casting.actions.rw.OpTheCoolerReadable;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexpose.iotas.ItemStackIota;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(OpTheCoolerReadable.class)
public abstract class OpTheCoolerReadableMixin implements ConstMediaAction {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true, remap = false)
    private void readFromItemStack(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
        if (args.get(0) instanceof ItemStackIota stack) {
            ADIotaHolder holder = IXplatAbstractions.INSTANCE.findDataHolder(stack.getStack());
            cir.setReturnValue(List.of(new BooleanIota(holder != null)));
        }
    }
}