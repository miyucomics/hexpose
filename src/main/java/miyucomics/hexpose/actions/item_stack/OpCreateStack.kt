package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.asActionResult
import miyucomics.hexpose.iotas.getIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries

object OpCreateStack : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registries.ITEM.containsId(id))
			throw MishapInvalidIota.of(args[0], 1, "item_id")
		val count = args.getPositiveInt(1, argc)
		return ItemStack(Registries.ITEM.get(id), count).asActionResult
	}
}