package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.world.World

class DimensionIota(val dimension: Identifier) : Iota(TYPE, dimension) {
	override fun isTruthy() = true
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is DimensionIota) && this.dimension == that.dimension
	override fun serialize(): NbtElement = NbtString.of(this.dimension.toString())

	companion object {
		val TYPE: IotaType<DimensionIota> = object : IotaType<DimensionIota>() {
			override fun color() = 0xff_db3f30.toInt()
			override fun display(tag: NbtElement) = tag.asString().asTranslatedComponent.formatted(Formatting.AQUA)
			override fun deserialize(tag: NbtElement, world: ServerWorld) = DimensionIota(Identifier(tag.asString()))
		}
	}
}

inline val World.asActionResult get() = listOf(DimensionIota(this.registryKey.value))

fun List<Iota>.getWorld(idx: Int, env: CastingEnvironment, argc: Int = 0): ServerWorld {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is DimensionIota)
		return env.world.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, x.dimension))!!
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "dimension")
}