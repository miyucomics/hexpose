package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.entity.mob.Angerable

object OpGetAngryTime : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = (args[0] as? EntityIota)?.entity ?: throw MishapInvalidIota.of(args[0], 0, "angerable")
		env.assertEntityInRange(entity)
		if (entity is Angerable)
			return entity.angerTime.asActionResult
		throw MishapInvalidIota.of(args[0], 0, "angerable")
	}
}