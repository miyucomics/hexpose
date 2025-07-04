package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexpose.iotas.getItemStack

class OpGetMaxMedia : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val stack = args.getItemStack(0, argc)
		val holder = IXplatAbstractions.INSTANCE.findMediaHolder(stack) ?: return listOf(NullIota())
		return listOf(DoubleIota(holder.maxMedia.toDouble() / MediaConstants.DUST_UNIT.toDouble()))
	}
}