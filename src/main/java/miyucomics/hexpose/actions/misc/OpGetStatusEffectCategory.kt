package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.getStatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

object OpGetStatusEffectCategory : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) = when (args.getStatusEffect(0, argc).category) {
		StatusEffectCategory.BENEFICIAL -> (1).asActionResult
		StatusEffectCategory.NEUTRAL -> (0).asActionResult
		StatusEffectCategory.HARMFUL -> (-1).asActionResult
		else -> throw IllegalStateException()
	}
}