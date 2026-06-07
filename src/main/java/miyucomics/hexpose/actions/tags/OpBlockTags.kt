package miyucomics.hexpose.actions.tags

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.TagIota
import miyucomics.hexpose.iotas.getBlockType
import miyucomics.hexpose.utils.coerceBlockType
import net.minecraft.entity.ItemEntity
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.util.math.BlockPos
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota

object OpBlockTags : ConstMediaAction {
	override val argc: Int = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> =
		Registries.BLOCK.getEntry(args.coerceBlockType(0, env, argc)).streamTags().map(::TagIota).toList().asActionResult
}