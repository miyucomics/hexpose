package miyucomics.hexpose.actions.processors

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.getBlockType
import miyucomics.hexpose.utils.coerceBlockType
import net.minecraft.block.Block

class OpGetBlockTypeData(private val process: (Block) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) = process(args.coerceBlockType(0, env, argc))
}