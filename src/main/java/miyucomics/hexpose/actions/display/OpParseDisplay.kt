package miyucomics.hexpose.actions.display

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.getDisplay
import miyucomics.hexpose.utils.TextUtils
import net.minecraft.text.Style
import net.minecraft.text.Text
import ram.talia.moreiotas.api.asActionResult

object OpParseDisplay : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val output = mutableListOf<Text>()
		TextUtils.disintegrateText(args.getDisplay(0, OpDisintegrateDisplay.argc), Style.EMPTY, output)
		return output.joinToString(transform = Text::getString).asActionResult
	}
}