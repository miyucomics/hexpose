package miyucomics.hexposition.patterns.item_stack

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexposition.iotas.asActionResult
import miyucomics.hexposition.iotas.getItemStack
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.decoration.ItemFrameEntity

class OpItemIota : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)
		return item.stack.asActionResult()
	}
}