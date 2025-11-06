package miyucomics.hexpose.actions.blockstates

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.identifier.getIdentifier
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.IntProperty

object OpQueryBlockProperty : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)
		val target = args.getIdentifier(1, argc).path
		val state = env.world.getBlockState(pos)
		val property = state.properties.firstOrNull { it.name == target } ?: return listOf(NullIota())
		return when (property) {
			is BooleanProperty -> state.get(property).asActionResult
			is DirectionProperty -> state.get(property).unitVector.asActionResult
			is EnumProperty -> state.get(property).ordinal.asActionResult
			is IntProperty -> state.get(property).asActionResult
			else -> return listOf(GarbageIota())
		}
	}
}