package miyucomics.hexpose.actions.display.chat

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveIntUnder
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.utils.ChatHandler

object OpGetMessageIndexed : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return ChatHandler.getIndexed(args.getPositiveIntUnder(0, 64, argc))
	}
}