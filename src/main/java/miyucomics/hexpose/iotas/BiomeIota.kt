package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import miyucomics.hexpose.utils.wordify
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome

class BiomeIota(val biome: RegistryKey<Biome>) : Iota(TYPE, biome) {
	override fun isTruthy() = true
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is BiomeIota) && this.biome == that.biome
	override fun serialize(): NbtElement = NbtString.of(biome.value.toString())

	companion object {
		val TYPE: IotaType<BiomeIota> = object : IotaType<BiomeIota>() {
			override fun color() = 0xff_db3f30.toInt()
			override fun display(tag: NbtElement) = tag.asString().wordify().formatted(Formatting.GREEN)
			override fun deserialize(tag: NbtElement, world: ServerWorld): BiomeIota = BiomeIota(RegistryKey.of(RegistryKeys.BIOME, Identifier(tag.asString())))
		}
	}
}

inline val RegistryKey<Biome>.asActionResult get() = listOf(BiomeIota(this))

fun List<Iota>.getBiome(idx: Int, env: CastingEnvironment, argc: Int = 0): RegistryKey<Biome> {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is BiomeIota)
		return x.biome
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "biome")
}