package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.TextIota
import miyucomics.hexpose.iotas.getItemStack
import net.minecraft.item.WrittenBookItem
import net.minecraft.text.Text

object OpBookSources : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val book = args.getItemStack(0, argc)
		if (book.item !is WrittenBookItem)
			return listOf(NullIota())
		return listOf(
			TextIota(Text.literal(book.orCreateNbt.getString(WrittenBookItem.AUTHOR_KEY))),
			DoubleIota(book.orCreateNbt.getInt(WrittenBookItem.GENERATION_KEY).toDouble())
		)
	}
}