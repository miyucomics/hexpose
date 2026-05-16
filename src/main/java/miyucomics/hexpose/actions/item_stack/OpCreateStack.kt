package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.getItemType
import miyucomics.hexpose.utils.coerceItemType
import net.minecraft.item.ItemStack
import ram.talia.moreiotas.api.asActionResult

object OpCreateStack : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment) = ItemStack(args.coerceItemType(0, env, argc), args.getPositiveInt(1, argc)).asActionResult
}