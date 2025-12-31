package miyucomics.hexpose.actions.display.style

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.DisplayIota
import miyucomics.hexpose.iotas.asActionResult
import net.minecraft.text.Text

object OpCreateDisplay : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (args[0] is DisplayIota)
			return Text.literal((args[0] as DisplayIota).getRoot()).asActionResult
        return args[0].display().asActionResult
	}
}