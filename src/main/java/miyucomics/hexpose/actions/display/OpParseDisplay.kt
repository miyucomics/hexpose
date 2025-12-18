package miyucomics.hexpose.actions.display

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.display.getDisplay
import miyucomics.hexpose.iotas.display.getRoot

object OpParseDisplay : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return try {
			args.getDisplay(0, argc).getRoot().toDouble().asActionResult
		} catch (_: NumberFormatException) { listOf(NullIota()) }
	}
}