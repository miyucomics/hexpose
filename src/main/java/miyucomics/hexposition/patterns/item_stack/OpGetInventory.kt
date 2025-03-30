package miyucomics.hexposition.patterns.item_stack

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import miyucomics.hexposition.iotas.ItemStackIota
import miyucomics.hexposition.iotas.asActionResult
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.entity.passive.AbstractHorseEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.vehicle.VehicleInventory

class OpGetInventory : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return when (val entity = args.getEntity(0, argc)) {
			is AbstractHorseEntity -> entity.items.stacks.map { ItemStackIota.createOptimized(it) }.asActionResult
			is ItemFrameEntity -> entity.heldItemStack.asActionResult()
			is PlayerEntity -> {
				if (env.castingEntity != entity)
					throw MishapOthersName(entity)
				entity.inventory.main.map { ItemStackIota.createOptimized(it) }.asActionResult
			}
			is VehicleInventory -> entity.inventory.map { ItemStackIota.createOptimized(it) }.asActionResult
			else -> listOf(NullIota())
		}
	}
}