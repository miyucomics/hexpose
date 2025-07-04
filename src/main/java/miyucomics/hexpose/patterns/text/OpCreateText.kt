package miyucomics.hexpose.patterns.text

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.TextIota
import miyucomics.hexpose.iotas.asActionResult
import miyucomics.hexpose.iotas.getIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.text.Text

class OpCreateText : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (args[0] is TextIota)
			return Text.literal((args[0] as TextIota).text.string).asActionResult
		return args[0].display().asActionResult
	}
}