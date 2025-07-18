package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.TextIota
import miyucomics.hexpose.iotas.getItemStack
import net.minecraft.item.WritableBookItem
import net.minecraft.item.WrittenBookItem
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text

object OpReadBook : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val book = args.getItemStack(0, argc)
		val pages = when (book.item) {
			is WritableBookItem -> book.nbt?.getList("pages", NbtElement.STRING_TYPE.toInt())?.map { TextIota(Text.literal(it.asString())) } ?: return listOf(NullIota())
			is WrittenBookItem -> book.nbt?.getList("pages", NbtElement.STRING_TYPE.toInt())?.map { TextIota(Text.Serializer.fromJson(it.asString())!!) } ?: return listOf(NullIota())
			else -> return listOf(NullIota())
		}
		return listOf(ListIota(pages))
	}
}