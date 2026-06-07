package miyucomics.hexpose.actions.misc

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.getBiome
import miyucomics.hexpose.utils.wordify
import net.minecraft.registry.Registries
import net.minecraft.village.VillagerType
import ram.talia.moreiotas.api.asActionResult

object OpVillagerTypeFromBiome : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val type = VillagerType.BIOME_TO_TYPE[args.getBiome(0)] ?: return listOf(NullIota())
		return Registries.VILLAGER_TYPE.getId(type).wordify().asActionResult
	}
}