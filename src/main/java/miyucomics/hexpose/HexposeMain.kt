package miyucomics.hexpose

import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexpose.iotas.*
import miyucomics.hexpose.utils.ChatHandler
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class HexposeMain : ModInitializer {
	override fun onInitialize() {
		Registry.register(HexIotaTypes.REGISTRY, id("biome"), BiomeIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("dimension"), DimensionIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("display"), DisplayIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("enchantment"), EnchantmentIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("status_effect"), StatusEffectIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("tag"), TagIota.TYPE)

		ChatHandler.init()
		HexposeActions.init()
	}

	companion object {
		fun id(string: String) = Identifier("hexpose", string)
	}
}