package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.server.world.ChunkLevelType
import net.minecraft.util.math.ChunkPos

object OpGetChunkLoaded : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val position = args.getBlockPos(0, argc)
		val pos = ChunkPos(position)
		val worldChunk = env.world.chunkManager.getWorldChunk(pos.x, pos.z) ?: return (-1).asActionResult
		return when (worldChunk.levelType) {
			ChunkLevelType.INACCESSIBLE -> 0
			ChunkLevelType.FULL -> 1
			ChunkLevelType.BLOCK_TICKING -> 2
			ChunkLevelType.ENTITY_TICKING -> 3
		}.asActionResult
	}
}