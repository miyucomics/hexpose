package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.identifier.getIdentifier
import miyucomics.hexpose.iotas.item_stack.getItemStack
import net.minecraft.command.argument.ItemStackArgumentType.itemStack
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.registry.Registries

object OpGetEnchantmentCompat : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val stack = args.getItemStack(0, argc)
		val enchantment = Registries.ENCHANTMENT.get(args.getIdentifier(1, argc)) ?: throw MishapInvalidIota.of(args[1], 0, "enchantment_id")
		return (enchantment.isAcceptableItem(stack) && EnchantmentHelper.isCompatible(EnchantmentHelper.get(stack).keys, enchantment)).asActionResult
	}
}