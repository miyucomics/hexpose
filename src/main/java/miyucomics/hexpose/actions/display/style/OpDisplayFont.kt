package miyucomics.hexpose.actions.display.style

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexpose.iotas.DisplayIota
import net.minecraft.util.Identifier
import kotlin.math.roundToInt

object OpDisplayFont : Action {
	val INT_TO_FONT = mapOf(
		0 to Identifier("default"),
		1 to Identifier("alt"),
		2 to Identifier("illageralt")
	)
	val FONT_TO_INT = INT_TO_FONT.keys.associateBy(INT_TO_FONT::get)

	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.isEmpty())
			throw MishapNotEnoughArgs(1, 0)

		val top = stack.last()
		if (top is DisplayIota) {
			stack.removeAt(stack.lastIndex)
			stack.add(DoubleIota(FONT_TO_INT[top.text.style.font]!!.toDouble()))
			return OperationResult(image.copy(stack = stack).withUsedOp(), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
		}

		if (stack.size == 1)
			throw MishapNotEnoughArgs(2, 1)

		val font = when (val raw = stack[stack.lastIndex]) {
			is DoubleIota -> {
				if (raw.double.roundToInt() !in 0 until FONT_TO_INT.size)
					throw MishapInvalidIota.of(raw, 0, "int.positive.less", FONT_TO_INT.size)
				INT_TO_FONT[raw.double.roundToInt()]
			}
			is NullIota -> null
            else -> throw MishapInvalidIota.of(raw, 0, "number_or_null")
        }

		val text = stack[stack.lastIndex - 1]
		if (text !is DisplayIota)
			throw MishapInvalidIota.ofType(text, 1, "text")

		stack.removeAt(stack.lastIndex)
		stack.removeAt(stack.lastIndex)
		stack.add(DisplayIota.createSanitized(text.text.copy().setStyle(text.text.style.withFont(font))))
		return OperationResult(image.copy(stack = stack).withUsedOp(), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}