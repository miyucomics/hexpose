package miyucomics.hexpose.utils.depots.implementations

import miyucomics.hexpose.HexposeMain
import miyucomics.hexpose.utils.depots.Depot
import miyucomics.hexpose.utils.depots.accesses.InventoryDepotAccess
import net.minecraft.inventory.Inventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class BlockInventoryDepot(val pos: BlockPos, val slot: Int) : Depot {
	override val type: Identifier = id
	override fun isValid(world: ServerWorld): Boolean {
		val blockEntity = world.getBlockEntity(pos) as? Inventory ?: return false
		return slot >= 0 && slot < blockEntity.size()
	}

	override fun serialize() = NbtCompound().apply {
		putLong("pos", pos.asLong())
		putInt("slot", slot)
	}

	override fun getInventoryAccess(world: ServerWorld): InventoryDepotAccess? {
		if (!isValid(world))
			return null
		return InventoryDepotAccess(world.getBlockEntity(pos) as Inventory, slot)
	}

	companion object {
		val id = HexposeMain.id("block_inventory")
		fun deserialize(compound: NbtCompound) =
			BlockInventoryDepot(BlockPos.fromLong(compound.getLong("pos")), compound.getInt("slot"))
	}
}