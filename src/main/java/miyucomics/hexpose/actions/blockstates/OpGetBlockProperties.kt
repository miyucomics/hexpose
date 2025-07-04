package miyucomics.hexpose.actions.blockstates

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.IdentifierIota
import net.minecraft.util.Identifier

class OpGetBlockProperties : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)
		return env.world
			.getBlockState(pos)
			.properties
			.map { IdentifierIota(Identifier("facet", it.name)) }
			.asActionResult
	}
}