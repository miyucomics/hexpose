package miyucomics.hexpose.actions.text

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexpose.iotas.TextIota
import miyucomics.hexpose.iotas.mapTextStyle
import net.minecraft.text.Style

class OpStyleText(val setOp: (List<Iota>, Style) -> Style, val getOp: (Style) -> Iota) : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.isEmpty())
			throw MishapNotEnoughArgs(1, 0)
		val final = stack.last()
		if (final is TextIota) {
			stack.removeLast()
			stack.add(getOp(final.text.style))
			val newImage = image.copy(stack = stack, opsConsumed = image.opsConsumed + 1)
			return OperationResult(newImage, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
		}

		if (stack.size == 1)
			throw MishapNotEnoughArgs(2, 1)
		val text = stack[stack.lastIndex - 1]
		if (text !is TextIota)
			throw MishapInvalidIota.ofType(text, 1, "text")

		val new = mapTextStyle(text.text) { setOp(stack.takeLast(2), text.text.style) }
		stack.removeLast()
		stack.removeLast()
		stack.add(TextIota(new))
		val newImage = image.copy(stack = stack, opsConsumed = image.opsConsumed + 1)
		return OperationResult(newImage, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}