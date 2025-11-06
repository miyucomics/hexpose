package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.identifier.asActionResult
import miyucomics.hexpose.iotas.identifier.getIdentifier
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.village.VillagerType

object OpVillagerTypeFromBiome : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val identifier = args.getIdentifier(0, argc)
		val type = VillagerType.BIOME_TO_TYPE[RegistryKey.of(RegistryKeys.BIOME, identifier)] ?: return listOf(NullIota())
		return Registries.VILLAGER_TYPE.getId(type).asActionResult
	}
}