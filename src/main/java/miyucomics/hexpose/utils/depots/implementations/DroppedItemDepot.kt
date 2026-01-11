package miyucomics.hexpose.utils.depots.implementations

import miyucomics.hexpose.HexposeMain
import miyucomics.hexpose.utils.depots.Depot
import miyucomics.hexpose.utils.depots.DepotAccess
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import java.util.*

class DroppedItemDepot(val uuid: UUID) : Depot {
	override val type: Identifier = id
	override fun isValid(world: ServerWorld) = world.getEntity(uuid) is ItemEntity

	override fun serialize() = NbtCompound().apply {
		putUuid("uuid", uuid)
	}

	override fun getInventoryAccess(world: ServerWorld): DroppedItemDepotAccess? {
		if (!isValid(world))
			return null
		return DroppedItemDepotAccess(world.getEntity(uuid) as ItemEntity)
	}

	companion object {
		val id = HexposeMain.id("item_entity")
		fun deserialize(compound: NbtCompound) =
			DroppedItemDepot(compound.getUuid("uuid"))
	}
}

class DroppedItemDepotAccess(val entity: ItemEntity) : DepotAccess {
	override var stack: ItemStack
		get() = entity.stack
		set(stack) {
			entity.stack = stack
			if (stack.isEmpty)
				entity.discard()
		}
}