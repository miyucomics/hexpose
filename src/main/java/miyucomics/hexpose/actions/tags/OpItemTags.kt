package miyucomics.hexpose.actions.tags

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.IdentifierIota
import miyucomics.hexpose.iotas.ItemStackIota
import net.minecraft.entity.ItemEntity
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey

object OpItemTags : ConstMediaAction {
	override val argc: Int = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val item = when (val iota = args[0]) {
			is EntityIota if iota.entity is ItemEntity -> {
				env.assertEntityInRange(iota.entity)
				(iota.entity as ItemEntity).stack.item
			}
			is ItemStackIota -> iota.stack.item
			is IdentifierIota if Registries.ITEM.containsId(iota.identifier) -> Registries.ITEM.get(iota.identifier)
			else -> throw MishapInvalidIota.of(iota, 0, "itemtype_coerceable")
		}
		return Registries.ITEM.getEntry(item).streamTags().map(TagKey<Item>::id).map(::IdentifierIota).toList().asActionResult
	}
}