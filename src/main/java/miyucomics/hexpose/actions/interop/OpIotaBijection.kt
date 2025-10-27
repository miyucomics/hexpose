package miyucomics.hexpose.actions.interop

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota

@Suppress("UNCHECKED_CAST")
class OpIotaBijection<A : Iota, B : Iota>(val name: String, val a: IotaType<A>, val b: IotaType<B>, val toA: (B) -> A?, val toB: (A) -> B?) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val iota = args[0]
		return when (iota.type) {
			a -> listOf(toB(iota as A) ?: NullIota())
			b -> listOf(toA(iota as B) ?: NullIota())
			else -> throw MishapInvalidIota.of(iota, 0, "hexcasting.mishap.invalid_value.iota_bijection.$name")
		}
	}
}