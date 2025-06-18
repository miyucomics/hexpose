package miyucomics.hexpose.patterns

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import net.minecraft.entity.projectile.ProjectileEntity

class OpShooter : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = args.getEntity(0, argc)
		env.assertEntityInRange(entity)
		if (entity !is ProjectileEntity)
			return listOf(NullIota())
		val shooter = entity.owner ?: return listOf(NullIota())
		if (env.isEntityInRange(shooter))
			return shooter.asActionResult
		return listOf(NullIota())
	}
}