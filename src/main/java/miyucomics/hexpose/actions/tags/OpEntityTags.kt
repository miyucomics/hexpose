package miyucomics.hexpose.actions.tags

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.IdentifierIota
import net.minecraft.entity.EntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey

object OpEntityTags : ConstMediaAction {
	override val argc: Int = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entityType = when (val iota = args[0]) {
			is EntityIota -> {
				env.assertEntityInRange(iota.entity)
				iota.entity.type
			}
			is IdentifierIota if Registries.ENTITY_TYPE.containsId(iota.identifier) -> Registries.ENTITY_TYPE.get(iota.identifier)
			else -> throw MishapInvalidIota.of(iota, 0, "entitytype_coerceable")
		}
		return Registries.ENTITY_TYPE.getEntry(entityType).streamTags().map(TagKey<EntityType<*>>::id).map(::IdentifierIota).toList().asActionResult
	}
}