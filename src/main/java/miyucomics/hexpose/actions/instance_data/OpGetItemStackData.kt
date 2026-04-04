package miyucomics.hexpose.actions.instance_data

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.item.ItemStack
import ram.talia.moreiotas.api.getItemStack

class OpGetItemStackData(private val process: (ItemStack) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) = process(args.getItemStack(0, argc))
}