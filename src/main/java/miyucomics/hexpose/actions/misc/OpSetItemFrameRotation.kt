package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getPositiveIntUnder
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import net.minecraft.entity.decoration.ItemFrameEntity

object OpSetItemFrameRotation : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val entity = args.getEntity(0, argc)
		env.assertEntityInRange(entity)
		if (entity !is ItemFrameEntity)
			throw MishapBadEntity.of(entity, "item_frame")
		return SpellAction.Result(Spell(entity, args.getPositiveIntUnder(1, 8, argc)), 0, listOf())
	}

	private data class Spell(val frame: ItemFrameEntity, val rotation: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			frame.rotation = rotation
		}
	}
}