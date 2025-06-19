package miyucomics.hexpose.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import jdk.incubator.vector.VectorShuffle.iota
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.VillagerEntity

class OpGetVillagerData(private val process: (VillagerEntity) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = args.getEntity(0, argc)
		env.assertEntityInRange(entity)
		if (entity !is VillagerEntity)
			throw MishapBadEntity.of(entity, "villager")
		return process(entity)
	}
}