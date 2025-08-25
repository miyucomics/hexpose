package miyucomics.hexpose.utils

class RingBuffer<T>(private val capacity: Int) {
	private val buffer = ArrayDeque<T>(capacity)

	fun add(item: T) {
		if (buffer.size == capacity)
			buffer.removeAt(0)
		buffer.addLast(item)
	}

	fun buffer() = buffer
	fun last() = buffer.lastOrNull()
	fun clear() = buffer.clear()
	fun size(): Int = buffer.size
}