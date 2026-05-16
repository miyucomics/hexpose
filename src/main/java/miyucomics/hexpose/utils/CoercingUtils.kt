package miyucomics.hexpose.utils

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import ram.talia.moreiotas.api.casting.iota.EntityTypeIota
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota

fun List<Iota>.coerceBlockType(idx: Int, env: CastingEnvironment, argc: Int = 0): Block =
	when (val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }) {
		is Vec3Iota -> {
			env.assertVecInRange(x.vec3)
			env.world.getBlockState(BlockPos.ofFloored(x.vec3)).block
		}
		is EntityIota if x.entity is ItemEntity && ((x.entity as ItemEntity).stack.item is BlockItem) -> {
			env.assertEntityInRange(x.entity)
			((x.entity as ItemEntity).stack.item as BlockItem).block
		}
		is ItemStackIota if x.itemStack.item is BlockItem -> (x.itemStack.item as BlockItem).block
		is ItemTypeIota if x.block != null -> x.block!!
		else -> throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "blocktype_coerceable")
	}

fun List<Iota>.coerceEntityType(idx: Int, env: CastingEnvironment, argc: Int = 0): EntityType<*> =
	when (val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }) {
		is EntityIota if env.isEntityInRange(x.entity) -> x.entity.type
		is EntityTypeIota -> x.entityType
		else -> throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "entitytype_coerceable")
	}

fun List<Iota>.coerceItemType(idx: Int, env: CastingEnvironment, argc: Int = 0): Item =
	when (val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }) {
		is EntityIota if x.entity is ItemEntity -> {
			env.assertEntityInRange(x.entity)
			(x.entity as ItemEntity).stack.item
		}
		is ItemStackIota -> x.itemStack.item
		is ItemTypeIota if x.item != null -> x.item!!
		else -> throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "itemtype_coerceable")
	}