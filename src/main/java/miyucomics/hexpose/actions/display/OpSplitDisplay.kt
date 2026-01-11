package miyucomics.hexpose.actions.display

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.DisplayIota
import miyucomics.hexpose.iotas.getDisplay
import miyucomics.hexpose.iotas.getRoot
import net.minecraft.text.Text

object OpSplitDisplay : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val source = args.getDisplay(0, argc)
		val delimiter = args.getDisplay(1, argc).getRoot()
		return source.getRoot().split(delimiter).map { splice -> Text.literal(splice).also { it.style = source.style } }.map(::DisplayIota).asActionResult
	}
}