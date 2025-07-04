package miyucomics.hexpose.actions.text

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.TextIota
import miyucomics.hexpose.iotas.getText
import miyucomics.hexpose.utils.TextUtils

class OpSplitText : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val text = args.getText(0, 1)
		return TextUtils.split(text).map { TextIota(it) }.asActionResult
	}
}