package miyucomics.hexpose.actions.processors

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.getEnchantment
import net.minecraft.enchantment.Enchantment

class OpGetEnchantmentTypeData(private val process: (Enchantment) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) = process(args.getEnchantment(0, argc))
}