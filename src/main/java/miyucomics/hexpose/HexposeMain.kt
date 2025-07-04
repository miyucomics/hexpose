package miyucomics.hexpose

import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexpose.inits.HexposePatterns
import miyucomics.hexpose.iotas.IdentifierIota
import miyucomics.hexpose.iotas.ItemStackIota
import miyucomics.hexpose.iotas.TextIota
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class HexposeMain : ModInitializer {
	override fun onInitialize() {
		Registry.register(HexIotaTypes.REGISTRY, id("identifier"), IdentifierIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("item_stack"), ItemStackIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("text"), TextIota.TYPE)
		HexposePatterns.init()
	}

	companion object {
		fun id(string: String) = Identifier("hexpose", string)
	}
}