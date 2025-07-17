package miyucomics.hexpose.utils

import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text


object TextUtils {
	fun split(text: Text): MutableList<Text> {
		val chars = mutableListOf<Text>()
		collectStyledCharacters(text, text.style, chars)
		return chars
	}

	private fun collectStyledCharacters(text: Text, parentStyle: Style, out: MutableList<Text>) {
		val effectiveStyle = text.style.withParent(parentStyle)
		val content = text.content
		if (content is LiteralTextContent)
			content.string.forEach { out += Text.literal(it.toString()).setStyle(effectiveStyle) }
		for (child in text.siblings)
			collectStyledCharacters(child, effectiveStyle, out)
	}

	fun optimize(input: Text): MutableText {
		val merged = input.siblings.fold(mutableListOf<MutableText>()) { acc, sibling ->
			val optimized = optimize(sibling)
			val last = acc.lastOrNull()
			if (last != null && last.style == optimized.style) {
				acc[acc.size - 1] = Text.literal(last.string + optimized.string).setStyle(last.style)
			} else {
				acc += optimized
			}
			acc
		}

		val head = merged.firstOrNull() ?: return Text.literal("")
		merged.drop(1).forEach { head.append(it) }
		return head
	}
}

fun List<Text>.mergeText(): MutableText = this.fold(Text.empty()) { acc, text -> acc.append(text) }