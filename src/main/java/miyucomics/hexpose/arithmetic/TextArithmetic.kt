package miyucomics.hexpose.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.casting.arithmetic.operator.nextPositiveIntUnder
import at.petrak.hexcasting.common.casting.arithmetic.operator.nextPositiveIntUnderInclusive
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexpose.iotas.TextIota
import miyucomics.hexpose.iotas.asActionResult
import miyucomics.hexpose.utils.TextUtils
import miyucomics.hexpose.utils.mergeText
import net.minecraft.text.Text
import kotlin.math.max
import kotlin.math.min

object TextArithmetic : Arithmetic {
	override fun arithName(): String = "text_arithmetic"
	override fun opTypes(): Iterable<HexPattern> = listOf(ADD, MUL, ABS, INDEX, SLICE, REV, INDEX_OF, REMOVE, REPLACE, UNIQUE)
	override fun getOperator(pattern: HexPattern): Operator = when (pattern) {
		ADD -> twoTextIntoIota { a, b -> TextIota(Text.empty().append(a).append(b)) }
		MUL -> textNumberIntoIota { text, number ->
			val newCopy = Text.empty()
			for (i in 0 until number.toInt()) newCopy.append(text)
			TextIota(newCopy)
		}
		ABS -> textIntoIota { DoubleIota(TextUtils.split(it).size.toDouble()) }
		INDEX -> textNumberIntoIota { text, number ->
			val x = TextUtils.split(text).getOrElse(number.toInt()) { return@textNumberIntoIota NullIota() }
			return@textNumberIntoIota TextIota(x)
		}
		SLICE -> object : OperatorBasic(3, IotaMultiPredicate.triple(IotaPredicate.ofType(TextIota.TYPE), IotaPredicate.ofType(HexIotaTypes.DOUBLE), IotaPredicate.ofType(HexIotaTypes.DOUBLE))) {
			override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
				val args = iotas.iterator().withIndex()
				val list = TextUtils.split((args.next().value as TextIota).text)
				val index0 = args.nextPositiveIntUnderInclusive(list.size, arity)
				val index1 = args.nextPositiveIntUnderInclusive(list.size, arity)
				if (index0 == index1)
					return Text.empty().asActionResult
				return list.subList(min(index0, index1), max(index0, index1)).mergeText().asActionResult
			}
		}
		REV -> textIntoIota { TextIota(TextUtils.split(it).reversed().mergeText()) }
		INDEX_OF -> twoTextIntoIota { text1, text2 ->
			val haystack = TextUtils.split(text1)
			val probe = TextUtils.split(text2)
			var index = 0.0
			haystack.windowed(probe.size, 1, false).forEach {
				if (it == probe)
					return@twoTextIntoIota DoubleIota(index)
				index++
			}
			return@twoTextIntoIota DoubleIota(-1.0)
		}
		REMOVE -> textNumberIntoIota { text, number ->
			val x = TextUtils.split(text)
			x.removeAt(number.toInt())
			return@textNumberIntoIota TextIota(x.mergeText())
		}
		REPLACE -> object : OperatorBasic(3, IotaMultiPredicate.triple(IotaPredicate.ofType(TextIota.TYPE), IotaPredicate.ofType(HexIotaTypes.DOUBLE), IotaPredicate.ofType(TextIota.TYPE))) {
			override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
				val args = iotas.iterator().withIndex()
				val list = TextUtils.split((args.next().value as TextIota).text)
				val index = args.nextPositiveIntUnder(list.size, arity)
				val fragment = (args.next().value as TextIota).text
				list[index] = fragment
				return list.mergeText().asActionResult
			}
		}
		UNIQUE -> textIntoIota { text ->
			val seen = mutableSetOf<Text>()
			val result = Text.empty()
			for (charText in TextUtils.split(text)) {
				if (seen.add(charText)) result.append(charText)
			}
			TextIota(result)
		}
		else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
	}

	private fun textIntoIota(op: (Text) -> Iota): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(TextIota.TYPE))) { i -> op((i as TextIota).text) }
	private fun twoTextIntoIota(op: (Text, Text) -> Iota): OperatorBinary = OperatorBinary(IotaMultiPredicate.all(IotaPredicate.ofType(TextIota.TYPE))) { i, j -> op((i as TextIota).text, (j as TextIota).text) }
	private fun textNumberIntoIota(op: (Text, Double) -> Iota): OperatorBinary = OperatorBinary(IotaMultiPredicate.pair(IotaPredicate.ofType(TextIota.TYPE), IotaPredicate.ofType(HexIotaTypes.DOUBLE))) { i, j -> op((i as TextIota).text, (j as DoubleIota).double) }
}