package miyucomics.hexpose.actions.text

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.TextIota
import miyucomics.hexpose.iotas.asActionResult
import net.minecraft.text.Text

class OpCreateText : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (args[0] is TextIota)
			return Text.literal((args[0] as TextIota).text.string).asActionResult
		return args[0].display().asActionResult
	}
}