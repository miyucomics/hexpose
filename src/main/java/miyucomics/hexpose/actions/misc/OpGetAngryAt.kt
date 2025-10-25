package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import net.minecraft.entity.Targeter
import net.minecraft.entity.mob.Angerable

object OpGetAngryAt : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = args.getEntity(0, argc)
		env.assertEntityInRange(entity)
		return when (entity) {
			is Angerable -> if (env.isEntityInRange(entity.target)) entity.target else null
			is Targeter -> if (env.isEntityInRange(entity.target)) entity.target else null
			else -> throw MishapBadEntity.of(entity, "targetting")
		}.asActionResult
	}
}