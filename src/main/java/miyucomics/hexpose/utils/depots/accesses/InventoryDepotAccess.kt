package miyucomics.hexpose.utils.depots.accesses

import miyucomics.hexpose.utils.depots.DepotAccess
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

// A depot access that wraps an inventory with a given slot
class InventoryDepotAccess(val inventory: Inventory, val slot: Int) : DepotAccess {
	override var stack: ItemStack
		get() = inventory.getStack(slot)
		set(stack) {
			inventory.setStack(slot, stack)
			inventory.markDirty()
		}

	override fun isValidForSlot(stack: ItemStack): Boolean = inventory.isValid(slot, stack)
}