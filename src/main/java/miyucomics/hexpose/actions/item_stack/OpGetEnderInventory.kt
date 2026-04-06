package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import net.minecraft.entity.player.PlayerEntity
import ram.talia.moreiotas.api.casting.iota.ItemStackIota

object OpGetEnderInventory : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.castingEntity !is PlayerEntity)
			throw MishapBadCaster()
		return (env.castingEntity as PlayerEntity).enderChestInventory.stacks.map(ItemStackIota::createFiltered).asActionResult
	}
}