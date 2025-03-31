package miyucomics.hexposition.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexposition.iotas.getItemStack
import net.minecraft.item.ItemStack

class OpGetItemStackData(private val process: (ItemStack) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) = process(args.getItemStack(0, argc))
}