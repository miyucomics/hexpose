package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.entity.LivingEntity

object OpGetAttacker : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val iota = args[0]
		if (iota !is EntityIota)
			throw MishapInvalidIota.of(iota, 0, "lenient_living")
		val entity = iota.entity
		env.assertEntityInRange(entity)
		if (entity !is LivingEntity)
			throw MishapInvalidIota.of(iota, 0, "lenient_living")
		return (if (env.isEntityInRange(entity.attacker)) entity.attacker else null).asActionResult
	}
}