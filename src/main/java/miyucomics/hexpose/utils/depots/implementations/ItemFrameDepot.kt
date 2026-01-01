package miyucomics.hexpose.utils.depots.implementations

import miyucomics.hexpose.HexposeMain
import miyucomics.hexpose.utils.depots.Depot
import miyucomics.hexpose.utils.depots.DepotAccess
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import java.util.*

class ItemFrameDepot(val uuid: UUID) : Depot {
	override val type: Identifier = id
	override fun isValid(world: ServerWorld) = world.getEntity(uuid) is ItemFrameEntity

	override fun serialize() = NbtCompound().apply {
		putUuid("uuid", uuid)
	}

	override fun getInventoryAccess(world: ServerWorld): ItemFrameDepotAccess? {
		if (!isValid(world))
			return null
		return ItemFrameDepotAccess(world.getEntity(uuid) as ItemFrameEntity)
	}

	companion object {
		val id = HexposeMain.id("item_frame")
		fun deserialize(compound: NbtCompound) =
			ItemFrameDepot(compound.getUuid("uuid"))
	}
}

class ItemFrameDepotAccess(val entity: ItemFrameEntity) : DepotAccess {
	override var stack: ItemStack
		get() = entity.heldItemStack
		set(stack) {
			entity.heldItemStack = stack
		}
}