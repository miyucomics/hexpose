package miyucomics.hexpose.actions.display

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.DisplayIota
import miyucomics.hexpose.iotas.getDisplay
import miyucomics.hexpose.iotas.getRoot
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Text

object OpDisintegrateDisplay : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) = disintegrate(args.getDisplay(0, argc).text).asActionResult

	private fun disintegrate(text: Text): List<DisplayIota> {
		val result = mutableListOf<DisplayIota>()
		text.getRoot().forEach { char ->
			val charText = MutableText.of(LiteralTextContent(char.toString()))
			charText.style = text.style
			result.add(DisplayIota(charText))
		}
		text.siblings.forEach { sibling ->
			result.addAll(disintegrate(sibling))
		}
		return result
	}
}