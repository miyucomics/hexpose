package miyucomics.hexposition.patterns.item_stack

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getIntBetween
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexposition.iotas.asActionResult
import miyucomics.hexposition.iotas.getIdentifier
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.util.Hand

class OpCreateStack : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registries.ITEM.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "item_id")
		val count = args.getPositiveInt(0, argc)
		return ItemStack(Registries.ITEM.get(id), count).asActionResult()
	}
}