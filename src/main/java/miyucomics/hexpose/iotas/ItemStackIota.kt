package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.Registries
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

class ItemStackIota(stack: ItemStack) : Iota(TYPE, stack) {
	override fun isTruthy() = !stack.isEmpty
	val stack = this.payload as ItemStack
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is ItemStackIota) && ItemStack.areEqual(this.stack, that.stack)

	override fun serialize(): NbtCompound {
		val nbt = NbtCompound()
		val item = Registries.ITEM.getId(stack.item)
		nbt.putString(TAG_STACK_ID, item.toString())
		nbt.putInt(TAG_STACK_COUNT, stack.count)
		if (stack.hasNbt())
			nbt.put(TAG_STACK_NBT, stack.nbt!!.copy())
		return nbt
	}

	companion object {
		const val TAG_STACK_ID: String = "hexpose:stack_id"
		const val TAG_STACK_COUNT: String = "hexpose:stack_count"
		const val TAG_STACK_NBT: String = "hexpose:stack_tag"

		var TYPE: IotaType<ItemStackIota> = object : IotaType<ItemStackIota>() {
			override fun color() = 0xff_fc0362.toInt()
			override fun display(tag: NbtElement): Text {
				val stack = deserialize(tag, null).stack
				if (stack.isEmpty)
					return Text.translatable("hexpose.item_stack.null").formatted(Formatting.GRAY)
				return Text.literal("[item:" + (tag as NbtCompound).getString(TAG_STACK_ID) + "]").append(Text.translatable("hexpose.item_stack.format", Text.empty().append(stack.name).formatted(stack.rarity.formatting), stack.count))
			}

			override fun deserialize(tag: NbtElement, world: ServerWorld?): ItemStackIota {
				val compound = tag as NbtCompound
				val item = Registries.ITEM.get(Identifier(compound.getString(TAG_STACK_ID)))
				val count = compound.getInt(TAG_STACK_COUNT)
				val stack = ItemStack(item, count)
				if (compound.contains(TAG_STACK_NBT))
					stack.nbt = compound.getCompound(TAG_STACK_NBT)
				return ItemStackIota(stack)
			}
		}

		fun createOptimized(originalStack: ItemStack): ItemStackIota {
			val stack = originalStack.copy()
			val nbt = stack.nbt ?: return ItemStackIota(stack)

			val queue: ArrayDeque<NbtElement> = ArrayDeque(listOf(nbt))
			while (queue.isNotEmpty()) {
				when (val next = queue.removeFirst()) {
					is NbtList -> queue.addAll(next)
					is NbtCompound -> {
						if (next.contains(TAG_STACK_ID)) {
							next.remove(TAG_STACK_ID)
							next.remove(TAG_STACK_COUNT)
							next.remove(TAG_STACK_NBT)
						}
						next.keys.mapNotNullTo(queue) { next.get(it) }
					}
				}
			}

			return ItemStackIota(stack)
		}
	}
}

inline val ItemStack.asActionResult get() = listOf(ItemStackIota.createOptimized(this))

fun List<Iota>.getItemStack(idx: Int, argc: Int = 0): ItemStack {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is ItemStackIota)
		return x.stack.copy()
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "item_stack")
}