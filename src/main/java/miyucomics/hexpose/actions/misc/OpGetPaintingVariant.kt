package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexpose.iotas.asActionResult
import net.minecraft.entity.decoration.painting.PaintingEntity
import net.minecraft.registry.Registries

object OpGetPaintingVariant : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = args.getEntity(0, argc)
		env.assertEntityInRange(entity)
		if (entity !is PaintingEntity)
			throw MishapBadEntity.of(entity, "painting")
		return Registries.PAINTING_VARIANT.getId(entity.variant.comp_349()).asActionResult
	}
}