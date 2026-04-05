package miyucomics.hexpose.actions.display

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.DisplayIota
import miyucomics.hexpose.iotas.asActionResult
import miyucomics.hexpose.iotas.getDisplay
import miyucomics.hexpose.utils.TextUtils
import net.minecraft.text.Style
import net.minecraft.text.Text
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.casting.iota.StringIota
import ram.talia.moreiotas.api.getString

object OpParseDisplay : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) = when (args[0]) {
		is DisplayIota -> {
			val output = mutableListOf<Text>()
			TextUtils.disintegrateText(args.getDisplay(0, argc), Style.EMPTY, output)
			output.joinToString(transform = Text::getString).asActionResult
		}
		is StringIota -> Text.literal(args.getString(0, argc)).asActionResult
		else -> throw MishapInvalidIota.of(args[0], 0, "display_or_string")
	}
}