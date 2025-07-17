package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asCompound
import miyucomics.hexpose.utils.TextUtils
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.function.Function

class TextIota(text: Text) : Iota(TYPE, TextUtils.optimize(text)) {
	override fun isTruthy() = true
	val text = this.payload as Text
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is TextIota) && this.text == that.text

	override fun serialize(): NbtElement {
		val serialized = Text.Serializer.toJson(text)
		println(serialized.length)
		if (serialized.length > 32000)
			return NbtCompound()
		return NbtCompound().also { it.putString("text", serialized) }
	}

	companion object {
		var TYPE: IotaType<TextIota> = object : IotaType<TextIota>() {
			override fun color() = 0xff_db3f30.toInt()
			override fun display(tag: NbtElement): Text {
				if (!tag.asCompound.contains("text"))
					return Text.literal("arimfexendrapuse").formatted(Formatting.DARK_GRAY, Formatting.OBFUSCATED);
				return Text.Serializer.fromJson((tag as NbtCompound).getString("text"))!!
			}
			override fun deserialize(tag: NbtElement, world: ServerWorld): TextIota? {
				if (!tag.asCompound.contains("text"))
					return null
				return TextIota(Text.Serializer.fromJson((tag as NbtCompound).getString("text"))!!)
			}
		}
	}
}

inline val Text.asActionResult get() = listOf(TextIota(this))

fun List<Iota>.getText(idx: Int, argc: Int = 0): Text {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is TextIota)
		return x.text.copy()
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "text")
}

fun mapTextStyle(text: Text, transformer: Function<Style, Style>): Text {
	val copy = text.copy().setStyle(transformer.apply(text.style))
	for (sibling in text.siblings)
		copy.append(mapTextStyle(sibling, transformer))
	return copy
}