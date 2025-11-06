package miyucomics.hexpose.actions.display

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.display.DisplayIota
import miyucomics.hexpose.iotas.display.getDisplay
import miyucomics.hexpose.utils.TextUtils
import net.minecraft.text.Style
import net.minecraft.text.Text

object OpDisintegrateDisplay : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val output = mutableListOf<Text>()
		TextUtils.collectStyledCharacters(args.getDisplay(0, argc).text, Style.EMPTY, output)
		return output.map(::DisplayIota).asActionResult
	}
}