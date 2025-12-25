package miyucomics.hexpose.actions.tags

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.identifier.IdentifierIota
import miyucomics.hexpose.iotas.item_stack.ItemStackIota
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object OpBlockTags : ConstMediaAction {
	override val argc: Int = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val blockType = when (val iota = args[0]) {
			is Vec3Iota -> {
				env.assertVecInRange(iota.vec3)
				env.world.getBlockState(BlockPos.ofFloored(iota.vec3)).block
			}
			is EntityIota if iota.entity is ItemEntity && ((iota.entity as ItemEntity).stack.item is BlockItem) -> {
				env.assertEntityInRange(iota.entity)
				((iota.entity as ItemEntity).stack.item as BlockItem).block
			}
			is ItemStackIota if iota.stack.item is BlockItem -> (iota.stack.item as BlockItem).block
			is IdentifierIota if Registries.BLOCK.containsId(iota.identifier) -> Registries.BLOCK.get(iota.identifier)
			else -> throw MishapInvalidIota.of(iota, 0, "blocktype_coerceable")
		}
		return Registries.BLOCK.getEntry(blockType).streamTags().map(TagKey<Block>::id).map(::IdentifierIota).toList().asActionResult
	}
}