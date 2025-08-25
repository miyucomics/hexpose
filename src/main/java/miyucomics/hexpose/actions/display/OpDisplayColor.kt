package miyucomics.hexpose.actions.display

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexpose.iotas.DisplayIota
import net.minecraft.text.TextColor
import net.minecraft.util.math.Vec3d

object OpDisplayColor : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.isEmpty())
			throw MishapNotEnoughArgs(1, 0)

		val top = stack.last()
		if (top is DisplayIota) {
			stack.removeAt(stack.lastIndex)
			val color = top.text.style.color?.let {
				val r = ((it.rgb shr 16) and 0xFF) / 255.0
				val g = ((it.rgb shr 8) and 0xFF) / 255.0
				val b = (it.rgb and 0xFF) / 255.0
				Vec3Iota(Vec3d(r, g, b))
			} ?: NullIota()

			stack.add(color)
			return OperationResult(image.copy(stack = stack).withUsedOp(), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
		}

		if (stack.size == 1)
			throw MishapNotEnoughArgs(2, 1)

		val text = stack[stack.lastIndex - 1]
		if (text !is DisplayIota)
			throw MishapInvalidIota.ofType(text, 1, "text")

		val color = when (val colorRaw = stack[stack.lastIndex]) {
			is Vec3Iota -> TextColor.fromRgb(colorRaw.vec3.let {
				(it.x.coerceIn(0.0, 1.0) * 255).toInt() shl 16 or
				(it.y.coerceIn(0.0, 1.0) * 255).toInt() shl 8 or
				(it.z.coerceIn(0.0, 1.0) * 255).toInt()
			})
			is NullIota -> null
			else -> throw MishapInvalidIota.of(colorRaw, 0, "vector_or_null")
		}

		stack.removeAt(stack.lastIndex)
		stack.removeAt(stack.lastIndex)
		stack.add(DisplayIota.createSanitized(text.text.copy().setStyle(text.text.style.withColor(color))))
		return OperationResult(image.copy(stack = stack).withUsedOp(), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}