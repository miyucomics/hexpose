package miyucomics.hexpose.patterns.misc

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexpose.iotas.asActionResult
import net.minecraft.entity.decoration.painting.PaintingEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.registry.Registries

class OpCatVariant : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = args.getEntity(0, argc)
		env.assertEntityInRange(entity)
		if (entity !is CatEntity)
			throw MishapBadEntity.of(entity, "cat")
		return entity.variant.comp_706.asActionResult()
	}
}