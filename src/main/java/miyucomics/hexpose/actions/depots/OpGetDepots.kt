package miyucomics.hexpose.actions.depots

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.ItemStackIota
import miyucomics.hexpose.utils.inventories.HopperEndpointRegistry
import miyucomics.hexpose.utils.inventories.ItemProvider

object OpGetDepots : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val source = HopperEndpointRegistry.resolve(args[0], env, null) as? ItemProvider
			?: throw MishapInvalidIota.of(args[0], 0, "item_provider")
		return source.getItems().map(ItemStackIota.Companion::createOptimized).asActionResult
	}
}