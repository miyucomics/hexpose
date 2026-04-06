package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.getEnchantment
import net.minecraft.enchantment.EnchantmentHelper
import ram.talia.moreiotas.api.getItemStack

object OpGetEnchantmentStrength : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val stack = args.getItemStack(0, argc)
		val enchantment = args.getEnchantment(1, argc)
		val data = EnchantmentHelper.get(stack)
		if (!data.containsKey(enchantment))
			return (0).asActionResult
		return data.getValue(enchantment).asActionResult
	}
}