package miyucomics.hexpose.utils

class RingBuffer<T>(private val capacity: Int) : Iterable<T> {
	val buffer = ArrayDeque<T>(capacity)
	override fun iterator(): Iterator<T> = buffer.iterator()

	fun add(item: T) {
		if (buffer.size == capacity)
			buffer.removeFirst()
		buffer.addLast(item)
	}

	fun last() = buffer.lastOrNull()
	fun clear() = buffer.clear()
	fun size(): Int = buffer.size
}