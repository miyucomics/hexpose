package miyucomics.hexpose.patterns.text

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.asActionResult
import miyucomics.hexpose.iotas.getText
import miyucomics.hexpose.iotas.mapTextStyle
import net.minecraft.text.Style

class OpStyleText(val argCount: Int, val modify: (List<Iota>, Style) -> Style) : ConstMediaAction {
	override val argc = argCount
	override fun execute(args: List<Iota>, env: CastingEnvironment) = mapTextStyle(args.getText(0, argCount)) { modify(args, it) }.asActionResult
}