package miyucomics.hexposition.inits

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf
import at.petrak.hexcasting.common.lib.hex.HexActions
import miyucomics.hexposition.HexpositionMain
import miyucomics.hexposition.iotas.IdentifierIota
import miyucomics.hexposition.iotas.asActionResult
import miyucomics.hexposition.patterns.*
import miyucomics.hexposition.patterns.identifier.OpClassify
import miyucomics.hexposition.patterns.identifier.OpIdentify
import miyucomics.hexposition.patterns.identifier.OpRecognize
import miyucomics.hexposition.patterns.types.OpGetBlockTypeData
import miyucomics.hexposition.patterns.types.OpGetFoodTypeData
import miyucomics.hexposition.patterns.types.OpGetItemTypeData
import net.minecraft.block.CandleBlock
import net.minecraft.block.SeaPickleBlock
import net.minecraft.block.TurtleEggBlock
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

object HexpositionPatterns {
	@JvmStatic
	fun init() {
		register("am_enlightened", "awqaqqq", HexDir.SOUTH_EAST, OpEnlightened())
		register("is_brainswept", "qqqaqqq", HexDir.SOUTH_EAST, OpBrainswept())

		register("perlin", "qawedqdq", HexDir.WEST, OpPerlin())

		register("block_hardness", "qaqqqqqeeeeedq", HexDir.EAST, OpGetBlockTypeData { block -> block.hardness.asActionResult })
		register("block_blast_resistance", "qaqqqqqewaawaawa", HexDir.EAST, OpGetBlockTypeData { block -> block.blastResistance.asActionResult })
		register("blockstate_waterlogged", "edeeeeeqwqqqqw", HexDir.SOUTH_EAST, OpGetBlockStateData { state ->
			state.entries[Properties.WATERLOGGED] ?: return@OpGetBlockStateData listOf(NullIota())
			return@OpGetBlockStateData state.get(Properties.WATERLOGGED).asActionResult
		})
		register("blockstate_rotation", "qaqqqqqwadeeed", HexDir.EAST, OpGetBlockStateData { state ->
			return@OpGetBlockStateData if (state.entries[Properties.FACING] != null) state.get(Properties.FACING).unitVector.asActionResult
			else if (state.entries[Properties.HORIZONTAL_FACING] != null) state.get(Properties.HORIZONTAL_FACING).unitVector.asActionResult
			else if (state.entries[Properties.VERTICAL_DIRECTION] != null) state.get(Properties.VERTICAL_DIRECTION).unitVector.asActionResult
			else if (state.entries[Properties.AXIS] != null) Direction.from(state.get(Properties.AXIS), Direction.AxisDirection.POSITIVE).unitVector.asActionResult
			else if (state.entries[Properties.HORIZONTAL_AXIS] != null) Direction.from(state.get(Properties.HORIZONTAL_AXIS), Direction.AxisDirection.POSITIVE).unitVector.asActionResult
			else if (state.entries[Properties.HOPPER_FACING] != null) state.get(Properties.HOPPER_FACING).unitVector.asActionResult
			else listOf(NullIota())
		})
		register("blockstate_crop", "qaqqqqqwaea", HexDir.EAST, OpGetBlockStateData { state ->
			return@OpGetBlockStateData if (state.entries[Properties.AGE_1] != null) (state.get(Properties.AGE_1)).asActionResult
			else if (state.entries[Properties.AGE_2] != null) (state.get(Properties.AGE_2).toDouble() / 2.0).asActionResult
			else if (state.entries[Properties.AGE_3] != null) (state.get(Properties.AGE_3).toDouble() / 3.0).asActionResult
			else if (state.entries[Properties.AGE_4] != null) (state.get(Properties.AGE_4).toDouble() / 4.0).asActionResult
			else if (state.entries[Properties.AGE_5] != null) (state.get(Properties.AGE_5).toDouble() / 5.0).asActionResult
			else if (state.entries[Properties.AGE_7] != null) (state.get(Properties.AGE_7).toDouble() / 7.0).asActionResult
			else if (state.entries[Properties.AGE_15] != null) (state.get(Properties.AGE_15).toDouble() / 15.0).asActionResult
			else if (state.entries[Properties.AGE_25] != null) (state.get(Properties.AGE_25).toDouble() / 25.0).asActionResult
			else if (state.entries[Properties.LEVEL_3] != null) (state.get(Properties.LEVEL_3).toDouble() / 3).asActionResult
			else if (state.entries[Properties.LEVEL_8] != null) (state.get(Properties.LEVEL_8).toDouble() / 8).asActionResult
			else if (state.entries[Properties.HONEY_LEVEL] != null) (state.get(Properties.HONEY_LEVEL).toDouble() / 15.0).asActionResult
			else if (state.entries[Properties.BITES] != null) (state.get(Properties.BITES).toDouble() / 6.0).asActionResult
			else listOf(NullIota())
		})
		register("blockstate_glow", "qaqqqqqwaeaeaeaeaea", HexDir.EAST, OpGetBlockStateData { state ->
			state.entries[Properties.LIT] ?: return@OpGetBlockStateData listOf(NullIota())
			return@OpGetBlockStateData state.get(Properties.LIT).asActionResult
		})
		register("blockstate_lock", "qaqqqeaqwdewd", HexDir.EAST, OpGetBlockStateData { state ->
			state.entries[Properties.OPEN] ?: return@OpGetBlockStateData listOf(NullIota())
			return@OpGetBlockStateData state.get(Properties.OPEN).asActionResult
		})
		register("blockstate_turn", "qaqqqqqwqqwqd", HexDir.EAST, OpGetBlockStateData{ state ->
			state.entries[Properties.ROTATION] ?: return@OpGetBlockStateData listOf(NullIota())
			return@OpGetBlockStateData state.get(Properties.ROTATION).asActionResult
		})
		register("blockstate_bunch", "qaqqqqqweeeeedeeqaqdeee", HexDir.EAST, OpGetBlockStateData { state ->
			when (state.block) {
				is CandleBlock -> {
					state.entries[Properties.CANDLES] ?: return@OpGetBlockStateData listOf(NullIota())
					return@OpGetBlockStateData state.get(Properties.CANDLES).asActionResult
				}
				is SeaPickleBlock -> {
					state.entries[Properties.PICKLES] ?: return@OpGetBlockStateData listOf(NullIota())
					return@OpGetBlockStateData state.get(Properties.PICKLES).asActionResult
				}
				is TurtleEggBlock -> {
					state.entries[Properties.EGGS] ?: return@OpGetBlockStateData listOf(NullIota())
					return@OpGetBlockStateData state.get(Properties.EGGS).asActionResult
				}
				else -> return@OpGetBlockStateData listOf(NullIota())
			}
		})
		register("blockstate_book", "qaqqqqqeawa", HexDir.EAST, OpGetBlockStateData { state ->
			return@OpGetBlockStateData if (state.entries[Properties.HAS_BOOK] != null) state.get(Properties.HAS_BOOK).asActionResult
			else if (state.entries[Properties.HAS_RECORD] != null) state.get(Properties.HAS_RECORD).asActionResult
			else if (state.entries[BlockAkashicBookshelf.HAS_BOOKS] != null) state.get(BlockAkashicBookshelf.HAS_BOOKS).asActionResult
			else listOf(NullIota())
		})

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
		register("is_burning", "qqwaqda", HexDir.EAST, OpGetEntityData { entity -> entity.isOnFire.asActionResult })
		register("burning_time", "eewdead", HexDir.WEST, OpGetEntityData { entity -> (entity.fireTicks.toDouble() / 20).asActionResult })
		register("is_wet", "qqqqwaadq", HexDir.SOUTH_WEST, OpGetEntityData { entity -> entity.isWet.asActionResult })
		register("get_health", "wddwaqqwawq", HexDir.SOUTH_EAST, OpGetLivingEntityData { entity -> entity.health.asActionResult })
		register("get_max_health", "wddwwawaeqwawq", HexDir.SOUTH_EAST, OpGetLivingEntityData { entity -> entity.maxHealth.asActionResult })
		register("get_air", "wwaade", HexDir.EAST, OpGetLivingEntityData { entity -> entity.air.asActionResult })
		register("get_max_air", "wwaadee", HexDir.EAST, OpGetLivingEntityData { entity -> entity.maxAir.asActionResult })
		register("is_sleeping", "aqaew", HexDir.NORTH_WEST, OpGetLivingEntityData { entity -> entity.isSleeping.asActionResult })
		register("is_sprinting", "eaq", HexDir.WEST, OpGetLivingEntityData { entity -> entity.isSprinting.asActionResult })
		register("is_baby", "awaqdwaaw", HexDir.SOUTH_WEST, OpGetLivingEntityData { entity -> entity.isBaby.asActionResult })
		register("breedable", "awaaqdqaawa", HexDir.EAST, OpGetWillingness())
		register("get_player_hunger", "qqqadaddw", HexDir.WEST, OpGetPlayerData { player -> player.hungerManager.foodLevel.asActionResult })
		register("get_player_saturation", "qqqadaddq", HexDir.WEST, OpGetPlayerData { player -> player.hungerManager.saturationLevel.asActionResult })

		register("env_ambit", "wawaw", HexDir.EAST, OpGetAmbit())
		register("env_staff", "waaq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is StaffCastEnv).asActionResult })
		register("env_offhand", "qaqqqwaaq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env.castingHand == Hand.MAIN_HAND).asActionResult })
		register("env_media", "dde", HexDir.WEST, OpGetEnvData { env -> ((Long.MAX_VALUE - env.extractMedia(Long.MAX_VALUE, true)).toDouble() / MediaConstants.DUST_UNIT.toDouble()).asActionResult })
		register("env_packaged_hex", "waaqwwaqqqqq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is PackagedItemCastEnv).asActionResult })
		register("env_circle", "waaqdeaqwqae", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is CircleCastEnv).asActionResult })

		register("edible", "adaqqqdd", HexDir.WEST, OpGetItemTypeData { item -> item.isFood.asActionResult })
		register("get_hunger", "adaqqqddqe", HexDir.WEST, OpGetFoodTypeData { food -> food.hunger.asActionResult })
		register("get_saturation", "adaqqqddqw", HexDir.WEST, OpGetFoodTypeData { food -> food.saturationModifier.asActionResult })
		register("is_meat", "adaqqqddaed", HexDir.WEST, OpGetFoodTypeData { food -> food.isMeat.asActionResult })
		register("is_snack", "adaqqqddaq", HexDir.WEST, OpGetFoodTypeData { food -> food.isSnack.asActionResult })

		register("identify", "qqqqqe", HexDir.NORTH_EAST, OpIdentify())
		register("recognize", "eeeeeq", HexDir.WEST, OpRecognize())
		register("classify", "edqdeq", HexDir.WEST, OpClassify())
		register("get_mainhand_stack", "qaqqqq", HexDir.NORTH_EAST, OpGetPlayerData { player -> listOf(if (player.mainHandStack.isEmpty) NullIota() else IdentifierIota(
			Registries.ITEM.getId(player.mainHandStack.item))) })
		register("get_offhand_stack", "edeeee", HexDir.NORTH_WEST, OpGetPlayerData { player -> listOf(if (player.offHandStack.isEmpty) NullIota() else IdentifierIota(
			Registries.ITEM.getId(player.offHandStack.item))) })

		register("count_stack", "qaqqwqqqw", HexDir.EAST, OpGetItemStackData { stack -> stack.count.asActionResult })
		register("count_max_stack", "edeeweeew", HexDir.WEST, OpGetItemTypeData { item -> item.maxCount.asActionResult })
		register("damage_stack", "eeweeewdeq", HexDir.NORTH_EAST, OpGetItemStackData { stack -> stack.damage.asActionResult })
		register("damage_max_stack", "qqwqqqwaqe", HexDir.NORTH_WEST, OpGetItemTypeData { item -> item.maxDamage.asActionResult })
		register("media_max_stack", "ddeaq", HexDir.EAST, OpGetMaxMedia())

		register("get_effects_entity", "wqqq", HexDir.SOUTH_WEST, OpGetLivingEntityData { entity ->
			val list = mutableListOf<Iota>()
			for (effect in entity.statusEffects)
				list.add(IdentifierIota(Registries.STATUS_EFFECT.getId(effect.effectType)!!))
			list.asActionResult
		})
		register("get_effects_item", "wqqqadee", HexDir.SOUTH_WEST, OpGetPrescription())
		register("get_effect_category", "wqqqaawd", HexDir.SOUTH_WEST, OpGetStatusEffectCategory())
		register("get_effect_amplifier", "wqqqaqwa", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData { instance -> instance.amplifier.asActionResult })
		register("get_effect_duration", "wqqqaqwdd", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData { instance -> (instance.duration.toDouble() / 20.0).asActionResult })

		register("get_weather", "eweweweweweeeaedqdqde", HexDir.WEST, OpGetWorldData { world -> (
				if (world.isThundering) 2.0
				else if (world.isRaining) 1.0
				else 0.0
				).asActionResult
		})
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
		register("get_media", "ddew", HexDir.WEST, OpGetMedia())
		register("get_einstein", "aqwawqwqqwqwqwqwqwq", HexDir.SOUTH_WEST, OpGetWorldData { world -> world.dimension.comp_645().asActionResult })
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(HexActions.REGISTRY, HexpositionMain.id(name), ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action))
}