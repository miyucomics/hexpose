package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import miyucomics.hexpose.utils.wordify
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

class TagIota(val tag: TagKey<*>) : Iota(TYPE, tag) {
	override fun isTruthy() = true
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is TagIota) && this.tag == that.tag
	override fun serialize(): NbtElement = NbtCompound().apply {
		putString("registry", tag.comp_326.registry.toString())
		putString("tag", tag.id.toString())
	}

	companion object {
		val TYPE: IotaType<TagIota> = object : IotaType<TagIota>() {
			override fun color() = 0xff_db3f30.toInt()
			override fun display(tag: NbtElement) = tag.asCompound.getString("tag").wordify().formatted(Formatting.GOLD)
			override fun deserialize(tag: NbtElement, world: ServerWorld): TagIota {
				val registry = Identifier(tag.asCompound.getString("registry"))
				val id = Identifier(tag.asCompound.getString("tag"))
				return TagIota(TagKey.of(RegistryKey.ofRegistry<Any>(registry), id))
			}
		}
	}
}

inline val TagKey<*>.asActionResult get() = listOf(TagIota(this))