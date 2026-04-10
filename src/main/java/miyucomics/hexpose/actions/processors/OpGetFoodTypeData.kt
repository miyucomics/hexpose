package miyucomics.hexpose.actions.processors

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.getItemType
import net.minecraft.item.FoodComponent

class OpGetFoodTypeData(private val process: (FoodComponent) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) = process(args.getItemType(0, argc).foodComponent ?: throw MishapInvalidIota.of(args[0], 0, "food_item"))
}