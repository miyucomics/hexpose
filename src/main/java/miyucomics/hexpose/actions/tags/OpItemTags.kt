package miyucomics.hexpose.actions.tags

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.TagIota
import net.minecraft.entity.ItemEntity
import net.minecraft.registry.Registries
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota

object OpItemTags : ConstMediaAction {
	override val argc: Int = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val item = when (val iota = args[0]) {
			is EntityIota if iota.entity is ItemEntity -> {
				env.assertEntityInRange(iota.entity)
				(iota.entity as ItemEntity).stack.item
			}
			is ItemStackIota -> iota.itemStack.item
			is ItemTypeIota if iota.item != null -> iota.item
			else -> throw MishapInvalidIota.of(iota, 0, "itemtype_coerceable")
		}
		return Registries.ITEM.getEntry(item).streamTags().map(::TagIota).toList().asActionResult
	}
}