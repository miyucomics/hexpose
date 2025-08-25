package miyucomics.hexpose.actions.display

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexpose.iotas.DisplayIota

object OpDisplayChildren : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.isEmpty())
			throw MishapNotEnoughArgs(1, 0)

		val top = stack.removeAt(stack.lastIndex)
		if (top is DisplayIota) {
			stack.add(ListIota(top.getChildren().map(DisplayIota::createSanitized)))
			return OperationResult(image.copy(stack = stack).withUsedOp(), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
		}

		if (top !is ListIota)
			throw MishapInvalidIota.of(top, 0, "display_list")
		if (!top.list.all { it is DisplayIota })
			throw MishapInvalidIota.of(top, 0, "display_list")

		val text = stack.removeAt(stack.lastIndex)
		if (text !is DisplayIota)
			throw MishapInvalidIota.ofType(text, 1, "display")

		stack.add(DisplayIota.createSanitized(text.getWithNewChildren(top.list.map { (it as DisplayIota).text })))
		return OperationResult(image.copy(stack = stack).withUsedOp(), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}