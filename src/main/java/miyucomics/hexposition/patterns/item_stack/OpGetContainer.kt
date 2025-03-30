package miyucomics.hexposition.patterns.item_stack

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexposition.iotas.ItemStackIota
import net.minecraft.inventory.Inventory
import net.minecraft.util.math.Vec3d

class OpGetContainer : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)

		val inventory = env.world.getBlockEntity(pos)
		if (inventory == null || inventory !is Inventory)
			return listOf(NullIota())

		return (0 until inventory.size()).map { ItemStackIota.createOptimized(inventory.getStack(it)) }.asActionResult
	}
}