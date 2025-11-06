package miyucomics.hexpose.actions.identifier

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.fabric.xplat.FabricXplatImpl
import miyucomics.hexpose.iotas.identifier.asActionResult

object OpClassify : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) =
		FabricXplatImpl.INSTANCE.iotaTypeRegistry.getId(args[0].type)!!.asActionResult
}