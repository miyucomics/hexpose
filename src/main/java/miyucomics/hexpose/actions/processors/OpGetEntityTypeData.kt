package miyucomics.hexpose.actions.processors

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.utils.coerceEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType

class OpGetEntityTypeData(private val process: (EntityType<*>) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> =
		process(args.coerceEntityType(0, env, argc))
}