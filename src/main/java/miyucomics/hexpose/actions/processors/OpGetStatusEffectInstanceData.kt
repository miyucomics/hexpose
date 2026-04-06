package miyucomics.hexpose.actions.processors

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.getStatusEffect
import net.minecraft.entity.effect.StatusEffectInstance

class OpGetStatusEffectInstanceData(private val process: (StatusEffectInstance) -> List<Iota>) : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = args.getLivingEntityButNotArmorStand(0, argc)
		env.assertEntityInRange(entity)
		val effect = args.getStatusEffect(1, argc)
		return process(entity.getStatusEffect(effect) ?: return listOf(NullIota()))
	}
}