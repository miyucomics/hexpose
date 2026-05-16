package miyucomics.hexpose.actions.tags

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.TagIota
import miyucomics.hexpose.utils.coerceItemType
import net.minecraft.entity.ItemEntity
import net.minecraft.registry.Registries
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota

object OpItemTags : ConstMediaAction {
	override val argc: Int = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> =
		Registries.ITEM.getEntry(args.coerceItemType(0, env, argc)).streamTags().map(::TagIota).toList().asActionResult
}