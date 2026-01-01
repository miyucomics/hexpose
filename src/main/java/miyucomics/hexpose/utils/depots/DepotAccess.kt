package miyucomics.hexpose.utils.depots


import net.minecraft.item.ItemStack
import kotlin.math.min

/**
 * Provides access to the inventory backing a depot.
 * Separated from Depot interface to avoid exposing inventory operations on invalid depots.
m */
interface DepotAccess {
	var stack: ItemStack

	fun isValidForSlot(stack: ItemStack): Boolean = true

	fun canInsert(stack: ItemStack): Boolean {
		val current = this.stack
		if (current.isEmpty)
			return isValidForSlot(stack)
		return ItemStack.canCombine(current, stack) && current.count < min(stack.maxCount, current.maxCount) && isValidForSlot(stack)
	}

	// returns the stack extracted
	fun extract(count: Int): ItemStack {
		val current = this.stack
		val toExtract = min(count, current.count)

		if (toExtract <= 0)
			return ItemStack.EMPTY

		val extracted = current.copyWithCount(toExtract)
		this.stack = current.copyWithCount(current.count - toExtract)
		return extracted
	}

	// returns the remainder if nothing could be inserted
	fun insert(stack: ItemStack): ItemStack {
		val current = this.stack

		if (current.isEmpty) {
			if (!isValidForSlot(stack)) {
				return stack
			}

			val toInsert = min(stack.count, stack.maxCount)
			this.stack = stack.copyWithCount(toInsert)
			return if (toInsert < stack.count) {
				stack.copyWithCount(stack.count - toInsert)
			} else {
				ItemStack.EMPTY
			}
		}

		if (ItemStack.canCombine(current, stack)) {
			val maxInsert = min(stack.maxCount, current.maxCount) - current.count
			val toInsert = min(maxInsert, stack.count)

			if (toInsert > 0) {
				this.stack = current.copyWithCount(current.count + toInsert)
				return if (toInsert < stack.count) {
					stack.copyWithCount(stack.count - toInsert)
				} else {
					ItemStack.EMPTY
				}
			}
		}

		return stack
	}
}