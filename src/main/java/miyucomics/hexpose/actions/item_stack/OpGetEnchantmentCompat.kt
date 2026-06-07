package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.EnchantmentIota
import miyucomics.hexpose.iotas.getEnchantment
import net.minecraft.enchantment.EnchantmentHelper
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.getItemStack

object OpGetEnchantmentCompat : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val enchantment = args.getEnchantment(1, argc)
		return when (args[0]) {
			is ItemStackIota -> {
				val stack = args.getItemStack(0, argc)
				(enchantment.isAcceptableItem(stack) && EnchantmentHelper.isCompatible(EnchantmentHelper.get(stack).keys, enchantment)).asActionResult
			}
			is EnchantmentIota -> args.getEnchantment(0, argc).canCombine(enchantment).asActionResult
			else -> throw MishapInvalidIota.of(args[0], 1, "enchantment_or_item")
		}
	}
}