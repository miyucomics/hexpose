package miyucomics.hexpose.actions.processors

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.getItemType
import miyucomics.hexpose.utils.coerceItemType
import net.minecraft.item.Item

class OpGetItemTypeData(private val process: (Item) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) =
		process(args.coerceItemType(0, env, argc))
}