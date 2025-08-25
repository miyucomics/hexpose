package miyucomics.hexpose.actions.display

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexpose.iotas.DisplayIota
import net.minecraft.text.Style

class OpDisplayBoolean(val getter: (Style) -> Boolean?, val setter: Style.(Boolean?) -> Style) : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.isEmpty())
			throw MishapNotEnoughArgs(1, 0)

		val top = stack.last()
		if (top is DisplayIota) {
			stack.removeAt(stack.lastIndex)
			val result = getter(top.text.style)
			when (result) {
                true -> stack.add(BooleanIota(true))
                false -> stack.add(BooleanIota(false))
                null -> stack.add(NullIota())
            }
			val newImage = image.copy(stack = stack, opsConsumed = image.opsConsumed + 1)
			return OperationResult(newImage, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
		}

		if (stack.size == 1)
			throw MishapNotEnoughArgs(2, 1)

        val arg = when (val raw = stack[stack.lastIndex]) {
			is BooleanIota -> raw.bool
			is NullIota -> null
			else -> throw MishapInvalidIota.of(raw, 0, "boolean_or_null")
		}

		val text = stack[stack.lastIndex - 1]
		if (text !is DisplayIota)
			throw MishapInvalidIota.ofType(text, 1, "text")

		stack.removeAt(stack.lastIndex)
		stack.removeAt(stack.lastIndex)
		stack.add(DisplayIota.createSanitized(text.text.copy().setStyle(text.text.style.setter(arg))))
		return OperationResult(image.copy(stack = stack).withUsedOp(), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}