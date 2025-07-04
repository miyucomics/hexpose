package miyucomics.hexpose.actions.text

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.asActionResult
import miyucomics.hexpose.utils.mergeText

class OpCombineList : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val list = args.getList(0, 1)
		return list.map { it.display() }.mergeText().asActionResult
	}
}