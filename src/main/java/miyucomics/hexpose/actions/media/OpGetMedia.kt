package miyucomics.hexpose.actions.media

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.passive.AllayEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.getItemStack

object OpGetMedia : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val media = when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				when (entity) {
					is ADMediaHolder -> entity.media
					is AllayEntity -> 100 * MediaConstants.DUST_UNIT
					is ItemEntity -> IXplatAbstractions.INSTANCE.findMediaHolder(entity.stack)?.media
					is ServerPlayerEntity -> (Long.MAX_VALUE - StaffCastEnv(entity, Hand.MAIN_HAND).extractMedia(Long.MAX_VALUE, true))
					else -> null
				}
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				val blockEntity = env.world.getBlockEntity(position)
				when {
					env.world.getBlockState(position).block is ADMediaHolder -> (env.world.getBlockState(position).block as ADMediaHolder).media
					blockEntity is ADMediaHolder -> blockEntity.media
					blockEntity is BlockEntityAbstractImpetus -> blockEntity.media
					else -> null
				}
			}
			is ItemStackIota -> IXplatAbstractions.INSTANCE.findMediaHolder(args.getItemStack(0, argc))?.media
			else -> null
		} ?: return listOf(NullIota())

		return (media / MediaConstants.DUST_UNIT).asActionResult
	}
}