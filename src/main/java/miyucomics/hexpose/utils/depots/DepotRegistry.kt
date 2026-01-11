package miyucomics.hexpose.utils.depots

import miyucomics.hexpose.utils.depots.implementations.BlockInventoryDepot
import miyucomics.hexpose.utils.depots.implementations.DroppedItemDepot
import miyucomics.hexpose.utils.depots.implementations.ItemFrameDepot
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier

object DepotRegistry {
	private val DESERIALIZERS: MutableMap<Identifier, (NbtCompound) -> Depot> = HashMap()

	fun init() {
		register(BlockInventoryDepot.id, BlockInventoryDepot::deserialize)
		register(ItemFrameDepot.id, ItemFrameDepot::deserialize)
		register(DroppedItemDepot.id, DroppedItemDepot::deserialize)
	}

	fun register(id: Identifier, deserializer: (NbtCompound) -> Depot) {
		DESERIALIZERS[id] = deserializer
	}

	fun deserialize(compound: NbtCompound): Depot {
		val deserializer = DESERIALIZERS[Identifier(compound.getString("type"))] ?: throw IllegalStateException("No deserializer?")
		return deserializer(compound)
	}

	fun serialize(depot: Depot) = depot.serialize().apply { putString("type", depot.type.toString()) }
}