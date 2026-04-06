package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import net.minecraft.block.entity.LecternBlockEntity
import net.minecraft.inventory.Inventory
import ram.talia.moreiotas.api.casting.iota.ItemStackIota

object OpGetContainer : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)
		val inventory = env.world.getBlockEntity(pos)
		if (inventory is LecternBlockEntity)
			return listOf(ItemStackIota.createFiltered(inventory.book))
		if (inventory == null || inventory !is Inventory)
			return listOf(NullIota())
		return (0 until inventory.size()).map { ItemStackIota.createFiltered(inventory.getStack(it)) }.asActionResult
	}
}