package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.registry.Registries
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

class StatusEffectIota(val effect: StatusEffect) : Iota(TYPE, effect) {
	override fun isTruthy() = true
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is StatusEffectIota) && this.effect == that.effect
	override fun serialize(): NbtElement = NbtString.of(Registries.STATUS_EFFECT.getId(this.effect).toString())

	companion object {
		val TYPE: IotaType<StatusEffectIota> = object : IotaType<StatusEffectIota>() {
			override fun color() = 0xff_db3f30.toInt()
			override fun display(tag: NbtElement) = Registries.STATUS_EFFECT.get(Identifier(tag.asString()))!!.translationKey.asTranslatedComponent.formatted(Formatting.RED)
			override fun deserialize(tag: NbtElement, world: ServerWorld) = StatusEffectIota(Registries.STATUS_EFFECT.get(Identifier(tag.asString()))!!)
		}
	}
}

inline val StatusEffect.asActionResult get() = listOf(StatusEffectIota(this))

fun List<Iota>.getStatusEffect(idx: Int, argc: Int = 0): StatusEffect {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is StatusEffectIota)
		return x.effect
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "status_effect")
}