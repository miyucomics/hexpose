package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.registry.Registries
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

class EnchantmentIota(val enchantment: Enchantment) : Iota(TYPE, enchantment) {
	override fun isTruthy() = true
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is EnchantmentIota) && this.enchantment == that.enchantment
	override fun serialize(): NbtElement = NbtString.of(Registries.ENCHANTMENT.getId(this.enchantment).toString())

	companion object {
		val TYPE: IotaType<EnchantmentIota> = object : IotaType<EnchantmentIota>() {
			override fun color() = 0xff_db3f30.toInt()
			override fun display(tag: NbtElement) = Registries.ENCHANTMENT.get(Identifier(tag.asString()))!!.translationKey.asTranslatedComponent.formatted(Formatting.BLUE)
			override fun deserialize(tag: NbtElement, world: ServerWorld) = EnchantmentIota(Registries.ENCHANTMENT.get(Identifier(tag.asString()))!!)
		}
	}
}

inline val Enchantment.asActionResult get() = listOf(EnchantmentIota(this))

fun List<Iota>.getEnchantment(idx: Int, argc: Int = 0): Enchantment {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is EnchantmentIota)
		return x.enchantment
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "enchantment")
}