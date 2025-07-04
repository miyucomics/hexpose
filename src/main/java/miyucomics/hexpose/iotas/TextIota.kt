package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Style
import net.minecraft.text.Text
import java.util.function.Function

class TextIota(text: Text) : Iota(TYPE, text) {
	override fun isTruthy() = true
	val text = this.payload as Text
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is TextIota) && this.text == that.text

	override fun serialize(): NbtElement {
		val compound = NbtCompound()
		compound.putString("text", Text.Serializer.toJson(text))
		return compound
	}

	companion object {
		var TYPE: IotaType<TextIota> = object : IotaType<TextIota>() {
			override fun color() = 0xff_db3f30.toInt()
			override fun display(tag: NbtElement) = Text.Serializer.fromJson((tag as NbtCompound).getString("text"))!!
			override fun deserialize(tag: NbtElement, world: ServerWorld) = TextIota(Text.Serializer.fromJson((tag as NbtCompound).getString("text"))!!)
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