package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.Items
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota

class BlockTypeSubiota(val internalBlock: Block) : ItemTypeIota(internalBlock)
class ItemTypeSubiota(val internalItem: Item) : ItemTypeIota(internalItem)

inline val Block.asActionResult get() = listOf(BlockTypeSubiota(this))
inline val Item.asActionResult get() = listOf(ItemTypeSubiota(this))

fun List<Iota>.getBlockType(idx: Int, argc: Int = 0): Block {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is BlockTypeSubiota)
		return x.internalBlock
	if (x is ItemTypeSubiota && x.internalItem is BlockItem)
		return x.internalItem.block
	if (x is ItemTypeIota)
		return x.block ?: throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "block_type")
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "block_type")
}

fun List<Iota>.getItemType(idx: Int, argc: Int = 0): Item {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is ItemTypeSubiota)
		return x.internalItem
	if (x is BlockTypeSubiota && x.internalBlock.asItem() != Items.AIR)
		return x.internalBlock.asItem()
	if (x is ItemTypeIota) {
		val item = x.item ?: throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "item_type")
		if (item == Items.AIR)
			throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "item_type")
		return item
	}
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "item_type")
}