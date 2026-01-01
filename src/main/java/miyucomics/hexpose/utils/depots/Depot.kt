package miyucomics.hexpose.utils.depots

import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier

/**
 * Represents a handle to a specific inventory slot.
 * May become invalid if the underlying inventory is removed or changed.
 */
interface Depot {
	val type: Identifier
	fun isValid(world: ServerWorld): Boolean
	fun serialize(): NbtCompound
	fun getInventoryAccess(world: ServerWorld): DepotAccess?
}