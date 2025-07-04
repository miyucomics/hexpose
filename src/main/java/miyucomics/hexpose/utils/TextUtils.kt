package miyucomics.hexpose.utils

import net.minecraft.text.LiteralTextContent
import net.minecraft.text.Style
import net.minecraft.text.Text
import kotlin.text.forEach

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
}

fun List<Text>.mergeText() = this.fold(Text.empty()) { acc, text -> acc.append(text) }