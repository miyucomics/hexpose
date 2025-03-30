package miyucomics.hexposition

import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexposition.inits.HexpositionPatterns
import miyucomics.hexposition.iotas.IdentifierIota
import miyucomics.hexposition.iotas.ItemStackIota
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class HexpositionMain : ModInitializer {
	override fun onInitialize() {
		Registry.register(HexIotaTypes.REGISTRY, id("item_stack"), ItemStackIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("identifier"), IdentifierIota.TYPE)
		HexpositionPatterns.init()
	}

	companion object {
		fun id(string: String) = Identifier("hexposition", string)
	}
}