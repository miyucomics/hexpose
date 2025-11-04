package miyucomics.hexpose.utils

import net.minecraft.text.*
import net.minecraft.util.Formatting
import net.minecraft.util.Language

object TextUtils {
	fun collectStyledCharacters(text: Text, parentStyle: Style, out: MutableList<Text>) {
		val effectiveStyle = text.style.withParent(parentStyle)
		val content = text.content
		if (content is LiteralTextContent)
			content.string.forEach { out += Text.literal(it.toString()).setStyle(effectiveStyle) }
		for (child in text.siblings)
			collectStyledCharacters(child, effectiveStyle, out)
	}
}

// a little utility method that returns a copy of a text with default values for styles so that it does not take on anything from its parent if one exists
fun Text.makeIndependent(): Text {
	var independentStyle = style
	if (style.color == null)
		independentStyle = independentStyle.withColor(Formatting.WHITE)
	if (style.bold == null)
		independentStyle = independentStyle.withBold(false)
	if (style.italic == null)
		independentStyle = independentStyle.withItalic(false)
	if (style.underlined == null)
		independentStyle = independentStyle.withUnderline(false)
	if (style.strikethrough == null)
		independentStyle = independentStyle.withStrikethrough(false)
	if (style.obfuscated == null)
		independentStyle = independentStyle.withObfuscated(false)
	if (style.font == null)
		independentStyle = independentStyle.withFont(Style.DEFAULT_FONT_ID)
	return this.copy().setStyle(independentStyle)
}

// nice little function that recursively explores and flattens Text into English literals
fun Text.sanitize(): Text {
	val sanitizedRoot: MutableText = when (val content = this.content) {
		is LiteralTextContent -> Text.literal(content.string)
		is TranslatableTextContent -> {
			val pattern = Language.getInstance().get(content.key)
			val args = content.args.map { arg ->
				when (arg) {
					is Text -> arg.sanitize().string
					else -> arg.toString()
				}
			}.toTypedArray()
			Text.literal(String.format(pattern, *args))
		}
		else -> Text.literal("arimfexendrapuse")
	}

	sanitizedRoot.style = this.style
		.withClickEvent(null)
		.withHoverEvent(null)
		.withInsertion(null)

	for (child in this.siblings)
		sanitizedRoot.append(child.sanitize())

	return sanitizedRoot
}