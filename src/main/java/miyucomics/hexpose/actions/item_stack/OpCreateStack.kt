package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.getItemType
import net.minecraft.item.ItemStack
import ram.talia.moreiotas.api.asActionResult

object OpCreateStack : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment) = ItemStack(args.getItemType(0, argc), args.getPositiveInt(1, argc)).asActionResult
}