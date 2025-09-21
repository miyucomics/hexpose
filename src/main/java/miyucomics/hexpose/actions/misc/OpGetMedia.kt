package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.api.utils.extractMedia
import at.petrak.hexcasting.api.utils.scanPlayerForMediaStuff
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexpose.iotas.ItemStackIota
import miyucomics.hexpose.iotas.getItemStack
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.passive.AllayEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.max
import kotlin.math.min

object OpGetMedia : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return listOf(when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				when (entity) {
					is AllayEntity -> DoubleIota(100.0)
					is ItemEntity -> {
						val holder = IXplatAbstractions.INSTANCE.findMediaHolder(entity.stack)
						if (holder == null)
							NullIota()
						else
							DoubleIota(holder.media.toDouble() / MediaConstants.DUST_UNIT.toDouble())
					}
					is ServerPlayerEntity -> {
						val sources = scanPlayerForMediaStuff(entity)
						var costLeft = Long.MAX_VALUE

						for (source in sources) {
							val found = extractMedia(source, costLeft, drainForBatteries = false, simulate = true)
							costLeft -= found
							if (costLeft <= 0)
								break
						}

						if (costLeft > 0) {
							val mediaToHealth = HexConfig.common().mediaToHealthRate()
							val healthToRemove = max(costLeft / mediaToHealth, 0.5)
							val simulatedRemovedMedia = MathHelper.ceil(min(entity.health.toDouble(), healthToRemove) * mediaToHealth).toLong()
							costLeft -= simulatedRemovedMedia
						}

						DoubleIota((Long.MAX_VALUE - costLeft).toDouble() / MediaConstants.DUST_UNIT.toDouble())
					}
					else -> NullIota()
				}
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				val blockEntity = env.world.getBlockEntity(position)

				val media = when {
					env.world.getBlockState(position).block is ADMediaHolder -> (env.world.getBlockState(position).block as ADMediaHolder).media
					blockEntity is ADMediaHolder -> (blockEntity as ADMediaHolder).media
					blockEntity is BlockEntityAbstractImpetus -> blockEntity.media
					else -> null
				}

				if (media != null)
					DoubleIota(media.toDouble() / MediaConstants.DUST_UNIT.toDouble())
				else
					NullIota()
			}
			is ItemStackIota -> {
				val stack = args.getItemStack(0, argc)
				val holder = IXplatAbstractions.INSTANCE.findMediaHolder(stack)
				if (holder == null)
					NullIota()
				else
					DoubleIota(holder.media.toDouble() / MediaConstants.DUST_UNIT.toDouble())
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "media_holding")
		})
	}
}