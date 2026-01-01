package miyucomics.hexpose.actions.depots

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.asActionResult
import miyucomics.hexpose.iotas.getDepotInventoryAccess

object OpGetStackFromDepot : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) =
		args.getDepotInventoryAccess(env.world, 0, argc).stack.asActionResult
}