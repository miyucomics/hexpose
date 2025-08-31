package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asCompound
import miyucomics.hexpose.utils.sanitize
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableTextContent
import net.minecraft.util.Formatting
import net.minecraft.util.Language

class DisplayIota(text: Text) : Iota(TYPE, text) {
	override fun isTruthy() = true
	val text = this.payload as Text
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is DisplayIota) && this.text == that.text

	fun getRoot() = this.text.getRoot()

	fun modifyRootBuilder(modifier: (StringBuilder) -> StringBuilder): DisplayIota {
		val builder = StringBuilder(getRoot())
		modifier(builder)
		return getWithNewRoot(builder.toString())
	}

	fun modifyRootString(modifier: (StringBuilder) -> String): DisplayIota {
		val builder = modifier(StringBuilder(getRoot()))
		return getWithNewRoot(builder)
	}

	fun getChildren(): List<Text> = this.text.siblings

	fun getWithNewRoot(root: String): DisplayIota {
		val result = MutableText.of(LiteralTextContent(root))
		result.style = this.text.style
		result.siblings.clear()
		result.siblings.addAll(this.text.siblings.map(Text::copy))
		return DisplayIota(result)
	}

	fun getWithNewChildren(children: List<Text>): Text {
		return this.text.copy().also {
			it.siblings.clear()
			it.siblings.addAll(children)
		}
	}

	override fun serialize(): NbtElement {
		val serialized = Text.Serializer.toJson(text)
		if (serialized.length > 32000)
			return NbtCompound()
		return NbtCompound().also { it.putString("text", serialized) }
	}

	companion object {
		var TYPE: IotaType<DisplayIota> = object : IotaType<DisplayIota>() {
			override fun color() = 0xff_db3f30.toInt()
			override fun display(tag: NbtElement): Text {
				if (!tag.asCompound.contains("text"))
					return Text.literal("arimfexendrapuse").formatted(Formatting.DARK_GRAY, Formatting.OBFUSCATED);
				return Text.Serializer.fromJson((tag as NbtCompound).getString("text"))!!
			}

			override fun deserialize(tag: NbtElement, world: ServerWorld): DisplayIota? {
				if (!tag.asCompound.contains("text"))
					return null
				return DisplayIota(Text.Serializer.fromJson((tag as NbtCompound).getString("text"))!!)
			}
		}

		fun createSanitized(text: Text) = DisplayIota(text.sanitize())
	}
}

inline val Text.asActionResult get() = listOf(DisplayIota.createSanitized(this))

fun List<Iota>.getDisplay(idx: Int, argc: Int = 0): DisplayIota {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is DisplayIota)
		return x
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "display")
}

fun Text.getRoot(): String {
	return when (val content = this.content) {
		is LiteralTextContent -> content.string
		is TranslatableTextContent -> String.format(Language.getInstance().get(content.key), content.args)
		else -> "arimfexendrapuse"
	}
}