package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexpose.iotas.StatusEffectIota
import miyucomics.hexpose.iotas.asActionResult
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.ShulkerBulletEntity
import net.minecraft.entity.projectile.WitherSkullEntity
import net.minecraft.entity.projectile.thrown.PotionEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.potion.PotionUtil
import ram.talia.moreiotas.api.casting.iota.ItemStackIota

object OpGetPrescription : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return when (val arg = args[0]) {
			is EntityIota -> {
				env.assertEntityInRange(arg.entity)
				when (val entity = arg.entity) {
					is ItemEntity -> handleItemStack(entity.stack, args).asActionResult
					is ArrowEntity -> (entity.potion.effects + entity.effects).map { StatusEffectIota(it.effectType) }.asActionResult
					is PotionEntity -> PotionUtil.getPotionEffects(entity.stack).map { StatusEffectIota(it.effectType) }.asActionResult
					is ShulkerBulletEntity -> StatusEffects.LEVITATION.asActionResult
					is WitherSkullEntity -> StatusEffects.WITHER.asActionResult
					else -> listOf()
				}
			}
			is ItemStackIota ->  handleItemStack(arg.itemStack, args).asActionResult
			else -> throw MishapInvalidIota.of(args[0], 0, "potion_holding")
		}
	}

	private fun handleItemStack(stack: ItemStack, args: List<Iota>): List<StatusEffectIota> {
		if (!(stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION) || stack.item.isFood || stack.isOf(Items.TIPPED_ARROW)))
			throw MishapInvalidIota.of(args[0], 0, "potion_holding")

		val effects = mutableSetOf<StatusEffect>()
		effects.addAll(PotionUtil.getPotion(stack).effects.map(StatusEffectInstance::getEffectType))
		effects.addAll(PotionUtil.getCustomPotionEffects(stack).map(StatusEffectInstance::getEffectType))
		val possibleFoodEffects = stack.item.foodComponent?.statusEffects?.map { it.first.effectType }
		if (possibleFoodEffects != null)
			effects.addAll(possibleFoodEffects)

		return effects.map(::StatusEffectIota)
	}
}