package miyucomics.hexpose.actions.identifier

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.identifier.asActionResult
import miyucomics.hexpose.iotas.item_stack.ItemStackIota
import net.minecraft.registry.Registries

object OpIdentify : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return when (val arg = args[0]) {
			is EntityIota -> {
				val entity = arg.entity
				env.assertEntityInRange(entity)
				Registries.ENTITY_TYPE.getId(entity.type).asActionResult
			}
			is Vec3Iota -> {
				val pos = args.getBlockPos(0, argc)
				env.assertPosInRange(pos)
				Registries.BLOCK.getId(env.world.getBlockState(pos).block).asActionResult
			}
			is ItemStackIota -> Registries.ITEM.getId(arg.stack.item).asActionResult
			else -> throw MishapInvalidIota.of(arg, 0, "identifiable")
		}
	}
}