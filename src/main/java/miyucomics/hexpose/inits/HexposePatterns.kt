package miyucomics.hexpose.inits

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.lib.hex.HexActions
import miyucomics.hexpose.HexposeMain
import miyucomics.hexpose.iotas.IdentifierIota
import miyucomics.hexpose.iotas.asActionResult
import miyucomics.hexpose.patterns.blockstates.OpGetBlockProperties
import miyucomics.hexpose.patterns.blockstates.OpQueryBlockProperty
import miyucomics.hexpose.patterns.identifier.OpClassify
import miyucomics.hexpose.patterns.identifier.OpIdentify
import miyucomics.hexpose.patterns.instance_data.OpGetBlockStateData
import miyucomics.hexpose.patterns.instance_data.OpGetEntityData
import miyucomics.hexpose.patterns.instance_data.OpGetEnvData
import miyucomics.hexpose.patterns.instance_data.OpGetItemStackData
import miyucomics.hexpose.patterns.instance_data.OpGetLivingEntityData
import miyucomics.hexpose.patterns.instance_data.OpGetPlayerData
import miyucomics.hexpose.patterns.instance_data.OpGetPositionData
import miyucomics.hexpose.patterns.instance_data.OpGetStatusEffectInstanceData
import miyucomics.hexpose.patterns.instance_data.OpGetVillagerData
import miyucomics.hexpose.patterns.instance_data.OpGetWorldData
import miyucomics.hexpose.patterns.item_stack.*
import miyucomics.hexpose.patterns.misc.OpBrainswept
import miyucomics.hexpose.patterns.misc.OpBreedable
import miyucomics.hexpose.patterns.misc.OpCatVariant
import miyucomics.hexpose.patterns.misc.OpEnlightened
import miyucomics.hexpose.patterns.misc.OpGetAmbit
import miyucomics.hexpose.patterns.misc.OpGetEnchantmentStrength
import miyucomics.hexpose.patterns.misc.OpGetMaxMedia
import miyucomics.hexpose.patterns.misc.OpGetMedia
import miyucomics.hexpose.patterns.misc.OpGetPrescription
import miyucomics.hexpose.patterns.misc.OpGetStatusEffectCategory
import miyucomics.hexpose.patterns.misc.OpPaintingVariant
import miyucomics.hexpose.patterns.misc.OpPerlin
import miyucomics.hexpose.patterns.misc.OpPetOwner
import miyucomics.hexpose.patterns.misc.OpShooter
import miyucomics.hexpose.patterns.misc.OpVillagerTypeFromBiome
import miyucomics.hexpose.patterns.raycast.OpFluidRaycast
import miyucomics.hexpose.patterns.raycast.OpFluidSurfaceRaycast
import miyucomics.hexpose.patterns.raycast.OpPiercingRaycast
import miyucomics.hexpose.patterns.raycast.OpPiercingSurfaceRaycast
import miyucomics.hexpose.patterns.types.OpGetBlockTypeData
import miyucomics.hexpose.patterns.types.OpGetFoodTypeData
import miyucomics.hexpose.patterns.types.OpGetItemTypeData
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

object HexposePatterns {
	@JvmStatic
	fun init() {
		register("am_enlightened", "awqaqqq", HexDir.SOUTH_EAST, OpEnlightened())
		register("is_brainswept", "qqqaqqq", HexDir.SOUTH_EAST, OpBrainswept())

		register("perlin", "qawedqdq", HexDir.WEST, OpPerlin())

		register("fluid_raycast", "wqqaqwede", HexDir.EAST, OpFluidRaycast())
		register("fluid_surface_raycast", "weedewqaq", HexDir.EAST, OpFluidSurfaceRaycast())
		register("piercing_raycast", "wqqddqeqddq", HexDir.EAST, OpPiercingRaycast())
		register("piercing_surface_raycast", "weeaaeqeaae", HexDir.EAST, OpPiercingSurfaceRaycast())

		register("block_hardness", "qaqqqqqeeeeedq", HexDir.EAST, OpGetBlockTypeData { block -> block.hardness.asActionResult })
		register("block_blast_resistance", "qaqqqqqewaawaawa", HexDir.EAST, OpGetBlockTypeData { block -> block.blastResistance.asActionResult })
		register("blockstate_rotation", "qaqqqqqwadeeed", HexDir.EAST, OpGetBlockStateData { state ->
			val candidates = listOf(
				Properties.FACING to { state.get(Properties.FACING).unitVector },
				Properties.HORIZONTAL_FACING to { state.get(Properties.HORIZONTAL_FACING).unitVector },
				Properties.VERTICAL_DIRECTION to { state.get(Properties.VERTICAL_DIRECTION).unitVector },
				Properties.AXIS to { Direction.from(state.get(Properties.AXIS), Direction.AxisDirection.POSITIVE).unitVector },
				Properties.HORIZONTAL_AXIS to { Direction.from(state.get(Properties.HORIZONTAL_AXIS), Direction.AxisDirection.POSITIVE).unitVector },
				Properties.HOPPER_FACING to { state.get(Properties.HOPPER_FACING).unitVector }
			)

			for ((prop, extractor) in candidates)
				if (prop in state.entries)
					return@OpGetBlockStateData extractor().asActionResult

			return@OpGetBlockStateData listOf(NullIota())
		})
		register("blockstate_crop", "qaqqqqqwaea", HexDir.EAST, OpGetBlockStateData { state ->
			val candidates = listOf(
				Properties.AGE_1 to 1.0,
				Properties.AGE_2 to 2.0,
				Properties.AGE_3 to 3.0,
				Properties.AGE_4 to 4.0,
				Properties.AGE_5 to 5.0,
				Properties.AGE_7 to 7.0,
				Properties.AGE_15 to 15.0,
				Properties.LEVEL_3 to 3.0,
				Properties.LEVEL_8 to 8.0,
				Properties.HONEY_LEVEL to 15.0,
				Properties.BITES to 6.0
			)

			for ((prop, divisor) in candidates)
				if (prop in state.entries)
					return@OpGetBlockStateData (state.get(prop).toDouble() / divisor).asActionResult

			return@OpGetBlockStateData listOf(NullIota())
		})
		register("get_blockstates", "qaqqqeqqqwqaww", HexDir.EAST, OpGetBlockProperties())
		register("query_blockstate", "qaqqqqqeawa", HexDir.EAST, OpQueryBlockProperty())

		register("get_enchantments", "waqeaeqawqwawaw", HexDir.WEST, OpGetItemStackData { stack ->
			var data = stack.enchantments
			if (stack.isOf(Items.ENCHANTED_BOOK))
				data = EnchantedBookItem.getEnchantmentNbt(stack)
			val enchantments = mutableListOf<IdentifierIota>()
			for ((enchantment, _) in EnchantmentHelper.fromNbt(data))
				enchantments.add(IdentifierIota(Registries.ENCHANTMENT.getId(enchantment)!!))
			enchantments.asActionResult
		})
		register("get_enchantment_strength", "waqwwqaweede", HexDir.WEST, OpGetEnchantmentStrength())

		register("entity_width", "dwe", HexDir.NORTH_WEST, OpGetEntityData { entity -> entity.width.asActionResult })
		register("theodolite", "wqaa", HexDir.EAST, OpGetEntityData { entity ->
			val upPitch = (-entity.pitch + 90) * (Math.PI.toFloat() / 180)
			val yaw = -entity.headYaw * (Math.PI.toFloat() / 180)
			val h = MathHelper.cos(yaw).toDouble()
			val j = MathHelper.cos(upPitch).toDouble()
			Vec3d(
				MathHelper.sin(yaw).toDouble() * j,
				MathHelper.sin(upPitch).toDouble(),
				h * j
			).asActionResult
		})
		register("get_health", "wddwaqqwawq", HexDir.SOUTH_EAST, OpGetLivingEntityData { entity -> entity.health.asActionResult })
		register("get_max_health", "wddwwawaeqwawq", HexDir.SOUTH_EAST, OpGetLivingEntityData { entity -> entity.maxHealth.asActionResult })
		register("burning", "eewdead", HexDir.WEST, OpGetEntityData { entity -> (entity.fireTicks.toDouble() / 20).asActionResult })
		register("is_wet", "qqqqwaadq", HexDir.SOUTH_WEST, OpGetEntityData { entity -> entity.isWet.asActionResult })
		register("get_air", "wwaade", HexDir.EAST, OpGetLivingEntityData { entity -> (entity.air.toDouble() / 20).asActionResult })
		register("get_max_air", "wwaadee", HexDir.EAST, OpGetLivingEntityData { entity -> (entity.maxAir.toDouble() / 20).asActionResult })
		register("is_sleeping", "aqaew", HexDir.NORTH_WEST, OpGetLivingEntityData { entity -> entity.isSleeping.asActionResult })
		register("is_sprinting", "eaq", HexDir.WEST, OpGetLivingEntityData { entity -> entity.isSprinting.asActionResult })
		register("is_baby", "awaqdwaaw", HexDir.SOUTH_WEST, OpGetLivingEntityData { entity -> entity.isBaby.asActionResult })
		register("breedable", "awaaqdqaawa", HexDir.EAST, OpBreedable())
		register("get_player_hunger", "qqqadaddw", HexDir.WEST, OpGetPlayerData { player -> player.hungerManager.foodLevel.asActionResult })
		register("get_player_saturation", "qqqadaddq", HexDir.WEST, OpGetPlayerData { player -> player.hungerManager.saturationLevel.asActionResult })
		register("entity_vehicle", "eqqedwewew", HexDir.EAST, OpGetEntityData { entity -> entity.vehicle.asActionResult })
		register("entity_passengers", "qeeqawqwqw", HexDir.EAST, OpGetEntityData { entity -> entity.passengerList.map { EntityIota(it) }.asActionResult })
		register("shooter", "aadedade", HexDir.EAST, OpShooter())
		register("pet_owner", "qdaqwawqeewde", HexDir.WEST, OpPetOwner())

		register("env_ambit", "wawaw", HexDir.EAST, OpGetAmbit())
		register("env_staff", "waaq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is StaffCastEnv).asActionResult })
		register("env_offhand", "qaqqqwaaq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env.castingHand == Hand.MAIN_HAND).asActionResult })
		register("env_packaged_hex", "waaqwwaqqqqq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is PackagedItemCastEnv).asActionResult })
		register("env_circle", "waaqdeaqwqae", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is CircleCastEnv).asActionResult })

		register("edible", "adaqqqdd", HexDir.WEST, OpGetItemTypeData { item -> item.isFood.asActionResult })
		register("get_hunger", "adaqqqddqe", HexDir.WEST, OpGetFoodTypeData { food -> food.hunger.asActionResult })
		register("get_saturation", "adaqqqddqw", HexDir.WEST, OpGetFoodTypeData { food -> food.saturationModifier.asActionResult })
		register("is_meat", "adaqqqddaed", HexDir.WEST, OpGetFoodTypeData { food -> food.isMeat.asActionResult })
		register("is_snack", "adaqqqddaq", HexDir.WEST, OpGetFoodTypeData { food -> food.isSnack.asActionResult })

		register("identify", "qqqqqe", HexDir.NORTH_EAST, OpIdentify())
		register("classify", "edqdeq", HexDir.WEST, OpClassify())

		register("get_stack", "edeedq", HexDir.WEST, OpItemIota())
		register("create_stack", "qaqqae", HexDir.EAST, OpCreateStack())
		register("get_mainhand", "qaqqqq", HexDir.NORTH_EAST, OpGetHeldStack(Hand.MAIN_HAND))
		register("get_offhand", "edeeee", HexDir.NORTH_WEST, OpGetHeldStack(Hand.OFF_HAND))
		register("get_armor", "qaqddqeeeeqd", HexDir.NORTH_EAST, OpGetArmor())
		register("get_ender_chest", "qaqdqaqdeeewedw", HexDir.NORTH_EAST, OpGetEnderInventory())
		register("get_inventory", "edeeeeeqdee", HexDir.WEST, OpGetInventory())
		register("get_block_inventory", "qaqqqqqeaqq", HexDir.EAST, OpGetContainer())
		register("count_stack", "qaqqwqqqw", HexDir.EAST, OpGetItemStackData { stack -> stack.count.asActionResult })
		register("count_max_stack", "edeeweeew", HexDir.WEST, OpGetItemTypeData { item -> item.maxCount.asActionResult })
		register("damage_stack", "eeweeewdeq", HexDir.NORTH_EAST, OpGetItemStackData { stack -> stack.damage.asActionResult })
		register("damage_max_stack", "qqwqqqwaqe", HexDir.NORTH_WEST, OpGetItemTypeData { item -> item.maxDamage.asActionResult })
		register("rarity", "wqqed", HexDir.NORTH_EAST, OpGetItemStackData { stack -> stack.rarity.ordinal.asActionResult })

		register("get_effects_entity", "wqqq", HexDir.SOUTH_WEST, OpGetLivingEntityData { entity ->
			val list = mutableListOf<Iota>()
			for (effect in entity.statusEffects)
				list.add(IdentifierIota(Registries.STATUS_EFFECT.getId(effect.effectType)!!))
			list.asActionResult
		})
		register("get_effects_item", "wqqqadee", HexDir.SOUTH_WEST, OpGetPrescription())
		register("get_effect_category", "wqqqaawd", HexDir.SOUTH_WEST, OpGetStatusEffectCategory())
		register("get_effect_amplifier", "wqqqaqwa", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData { it.amplifier.asActionResult })
		register("get_effect_duration", "wqqqaqwdd", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData { it.duration.asActionResult })

		register("villager_level", "qeqwqwqwqwqeqawdaeaeaeaeaea", HexDir.EAST, OpGetVillagerData { villager -> villager.villagerData.level.asActionResult })
		register("villager_profession", "qeqwqwqwqwqeqawewawqwawadeeeee", HexDir.EAST, OpGetVillagerData { villager -> Registries.VILLAGER_PROFESSION.getId(villager.villagerData.profession).asActionResult() })
		register("villager_type", "qeqwqwqwqwqeqaweqqqqq", HexDir.EAST, OpGetVillagerData { villager -> Registries.VILLAGER_TYPE.getId(villager.villagerData.type).asActionResult() })
		register("biome_to_villager", "qeqwqwqwqwqeqawewwqqwwqwwqqww", HexDir.EAST, OpVillagerTypeFromBiome())

		register("get_media", "ddew", HexDir.WEST, OpGetMedia())
		register("env_media", "dde", HexDir.WEST, OpGetEnvData { env -> ((Long.MAX_VALUE - env.extractMedia(Long.MAX_VALUE, true)).toDouble() / MediaConstants.DUST_UNIT.toDouble()).asActionResult })
		register("media_max_stack", "ddeaq", HexDir.EAST, OpGetMaxMedia())

		register("get_weather", "eweweweweweeeaedqdqde", HexDir.WEST, OpGetWorldData { world -> (if (world.isThundering) 2.0 else if (world.isRaining) 1.0 else 0.0).asActionResult })
		register("get_light", "wqwqwqwqwqwaeqqqqaeqaeaeaeaw", HexDir.SOUTH_WEST, OpGetPositionData { world, position -> world.getLightLevel(position).asActionResult })
		register("get_power", "qwqwqwqwqwqqwwaadwdaaww", HexDir.EAST, OpGetPositionData { world, position -> world.getReceivedRedstonePower(position).asActionResult })
		register("get_comparator", "eweweweweweewwddawaddww", HexDir.WEST, OpGetPositionData { world, position ->
			val state = world.getBlockState(position)
			if (state.hasComparatorOutput())
				return@OpGetPositionData state.getComparatorOutput(world, position).asActionResult
			return@OpGetPositionData listOf(NullIota())
		})
		register("get_day", "wwawwawwqqawwdwwdwwaqwqwqwqwq", HexDir.SOUTH_EAST, OpGetWorldData { world -> (world.timeOfDay.toDouble() / 24000.0).asActionResult })
		register("get_time", "wddwaqqwqaddaqqwddwaqqwqaddaq", HexDir.SOUTH_EAST, OpGetWorldData { world -> world.time.asActionResult })
		register("get_moon", "eweweweweweeweeedadw", HexDir.WEST, OpGetWorldData { world -> world.moonSize.asActionResult })
		register("get_biome", "qwqwqawdqqaqqdwaqwqwq", HexDir.WEST, OpGetPositionData { world, position -> world.getBiome(position).key.get().value.asActionResult() })
		register("get_dimension", "qwqwqwqwqwqqaedwaqd", HexDir.WEST, OpGetWorldData { world -> world.registryKey.value.asActionResult() })
		register("get_einstein", "aqwawqwqqwqwqwqwqwq", HexDir.SOUTH_WEST, OpGetWorldData { world -> world.dimension.comp_645().asActionResult })

		register("cat_variant", "wqwqqwqwawaaw", HexDir.SOUTH_WEST, OpCatVariant())
		register("painting_variant", "wawwwqwwawwwqadaqeda", HexDir.SOUTH_WEST, OpPaintingVariant())
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(HexActions.REGISTRY, HexposeMain.id(name), ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action))
}
