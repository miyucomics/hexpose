package miyucomics.hexpose.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.styledWith
import miyucomics.hexpose.utils.depots.Depot
import miyucomics.hexpose.utils.depots.DepotAccess
import miyucomics.hexpose.utils.depots.DepotRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class DepotIota(depot: Depot) : Iota(TYPE, depot) {
	override fun isTruthy() = true
	val depot = this.payload as Depot
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is DepotIota) && this.depot == that.depot
	override fun serialize() = DepotRegistry.serialize(this.depot)

	companion object {
		var TYPE: IotaType<DepotIota> = object : IotaType<DepotIota>() {
			override fun color() = 0xff_e31426.toInt()
			override fun display(tag: NbtElement): Text {
				return Text.literal("depot").styledWith(Formatting.RED)
			}

			override fun deserialize(tag: NbtElement, world: ServerWorld) = DepotIota(DepotRegistry.deserialize(tag as NbtCompound))
		}
	}
}

inline val Depot.asActionResult get() = listOf(DepotIota(this))

fun List<Iota>.getDepot(idx: Int, argc: Int = 0): Depot {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is DepotIota)
		return x.depot
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "depot")
}

fun List<Iota>.getDepotInventoryAccess(world: ServerWorld, idx: Int, argc: Int = 0): DepotAccess {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is DepotIota)
		return x.depot.getInventoryAccess(world) ?: throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "valid_depot")
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "depot")
}