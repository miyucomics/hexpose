package miyucomics.hexpose.actions.types

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.getIdentifier
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.Registries

class OpGetEnchantmentTypeData(private val process: (Enchantment) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registries.ENCHANTMENT.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "enchantment_id")
		return process(Registries.ENCHANTMENT.get(id) ?: throw MishapInvalidIota.of(args[0], 0, "enchantment_id"))
	}
}