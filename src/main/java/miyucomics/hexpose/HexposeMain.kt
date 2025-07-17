package miyucomics.hexpose

import at.petrak.hexcasting.common.lib.hex.HexArithmetics
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexpose.arithmetic.TextArithmetic
import miyucomics.hexpose.iotas.IdentifierIota
import miyucomics.hexpose.iotas.ItemStackIota
import miyucomics.hexpose.iotas.TextIota
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class HexposeMain : ModInitializer {
	override fun onInitialize() {
		Registry.register(HexIotaTypes.REGISTRY, id("identifier"), IdentifierIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("item_stack"), ItemStackIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("text"), TextIota.TYPE)
		Registry.register(HexArithmetics.REGISTRY, id("text"), TextArithmetic)
		HexposeActions.init()

		ServerMessageEvents.CHAT_MESSAGE.register { message, sender, params ->
			val senderName = sender.name
			val chatMessage = message.content
			val timestamp = message.timestamp
		}
	}

	companion object {
		fun id(string: String) = Identifier("hexpose", string)
	}
}