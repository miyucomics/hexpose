package miyucomics.hexpose.utils

import at.petrak.hexcasting.api.HexAPI
import net.minecraft.server.network.ServerPlayerEntity

object CastingUtils {
	@JvmStatic
	fun isEnlightened(player: ServerPlayerEntity): Boolean {
		val advancement = player.getServer()!!.advancementLoader[HexAPI.modLoc("enlightenment")]
		val tracker = player.advancementTracker
		if (tracker.getProgress(advancement) != null)
			return tracker.getProgress(advancement).isDone
		return false
	}
}