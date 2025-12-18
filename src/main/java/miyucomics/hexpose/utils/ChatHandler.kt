package miyucomics.hexpose.utils

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.display.DisplayIota
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.minecraft.text.Text
import java.time.Duration
import java.time.Instant
import kotlin.math.floor

object ChatHandler {
	fun init() {
		ServerMessageEvents.CHAT_MESSAGE.register { message, sender, _ -> chatLog.add(Message(sender.name, message.content, message.timestamp)) }
	}

	fun getIndexed(n: Int): List<Iota> {
		if (n < 0 || n >= chatLog.buffer.size)
			return listOf(NullIota())
		val indexFromStart = chatLog.buffer.size - 1 - n
		return chatLog.buffer.elementAt(indexFromStart).intoHex(Instant.now())
	}

	fun getLast(): List<Iota> = chatLog.last()?.intoHex(Instant.now()) ?: listOf(NullIota())

	val chatLog = RingBuffer<Message>(64)
	data class Message(val sender: Text, val message: Text, val timestamp: Instant) {
		fun intoHex(now: Instant) = listOf(DisplayIota.createSanitized(sender), DisplayIota.createSanitized(message), DoubleIota(floor(Duration.between(now, timestamp).toMillis() / -50.0)))
	}
}