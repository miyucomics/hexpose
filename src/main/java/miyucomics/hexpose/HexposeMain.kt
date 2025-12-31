package miyucomics.hexpose

import at.petrak.hexcasting.common.lib.hex.HexArithmetics
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexpose.actions.display.arithmetic.DisplayArithmetic
import miyucomics.hexpose.utils.inventories.HopperEndpointRegistry
import miyucomics.hexpose.iotas.DisplayIota
import miyucomics.hexpose.iotas.IdentifierIota
import miyucomics.hexpose.iotas.ItemStackIota
import miyucomics.hexpose.utils.ChatHandler
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class HexposeMain : ModInitializer {
	override fun onInitialize() {
		Registry.register(HexIotaTypes.REGISTRY, id("identifier"), IdentifierIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("item_stack"), ItemStackIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("text"), DisplayIota.TYPE)
		Registry.register(HexArithmetics.REGISTRY, id("display"), DisplayArithmetic)
		ChatHandler.init()
		HexposeActions.init()
		HopperEndpointRegistry.init()
	}

	companion object {
		fun id(string: String) = Identifier("hexpose", string)
	}
}