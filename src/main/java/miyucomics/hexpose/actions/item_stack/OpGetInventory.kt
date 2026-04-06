package miyucomics.hexpose.actions.item_stack

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.entity.passive.AbstractHorseEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.vehicle.VehicleInventory
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.casting.iota.ItemStackIota

object OpGetInventory : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return when (val entity = args.getEntity(0, argc)) {
			is AbstractHorseEntity -> entity.items.stacks.map(ItemStackIota::createFiltered).asActionResult
			is ItemFrameEntity -> entity.heldItemStack.asActionResult
			is PlayerEntity -> {
				if (env.castingEntity != entity)
					throw MishapOthersName(entity)
				entity.inventory.main.map(ItemStackIota::createFiltered).asActionResult
			}
			is VehicleInventory -> entity.inventory.map(ItemStackIota::createFiltered).asActionResult
			else -> listOf(NullIota())
		}
	}
}