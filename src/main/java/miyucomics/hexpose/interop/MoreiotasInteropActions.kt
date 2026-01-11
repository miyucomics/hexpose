package miyucomics.hexpose.interop

import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexpose.HexposeActions.register
import miyucomics.hexpose.actions.interop.OpIotaBijection
import miyucomics.hexpose.iotas.IdentifierIota
import miyucomics.hexpose.iotas.ItemStackIota
import net.minecraft.registry.Registries
import ram.talia.moreiotas.api.casting.iota.EntityTypeIota
import ram.talia.moreiotas.api.casting.iota.IotaTypeIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota

object MoreiotasInteropActions {
	fun init() {
		register("moreiotas_entitytype_compat", "adwaq", HexDir.EAST, OpIotaBijection<IdentifierIota, EntityTypeIota>("entitytype", IdentifierIota.TYPE, EntityTypeIota.TYPE,
			{ IdentifierIota(Registries.ENTITY_TYPE.getId(it.entityType)) },
			{ if (Registries.ENTITY_TYPE.containsId(it.identifier)) EntityTypeIota(Registries.ENTITY_TYPE.get(it.identifier)) else null }
		))

		register("moreiotas_iotatype_compat", "adwawd", HexDir.EAST, OpIotaBijection<IdentifierIota, IotaTypeIota>("iotatype", IdentifierIota.TYPE, IotaTypeIota.TYPE,
			{ IdentifierIota(IXplatAbstractions.INSTANCE.iotaTypeRegistry.getId(it.iotaType)!!) },
			{ if (IXplatAbstractions.INSTANCE.iotaTypeRegistry.containsId(it.identifier)) IotaTypeIota(IXplatAbstractions.INSTANCE.iotaTypeRegistry.get(it.identifier)!!) else null }
		))

		register("moreiotas_itemtype_compat", "adwaqa", HexDir.EAST, OpIotaBijection<IdentifierIota, ItemTypeIota>("itemtype", IdentifierIota.TYPE, ItemTypeIota.TYPE,
			{ it.item?.let { item -> IdentifierIota(Registries.ITEM.getId(item)) } ?: IdentifierIota(Registries.BLOCK.getId(it.block)) },
			{ identifierIota -> when {
				Registries.ITEM.containsId(identifierIota.identifier) -> ItemTypeIota(Registries.ITEM.get(identifierIota.identifier))
				Registries.BLOCK.containsId(identifierIota.identifier) -> ItemTypeIota(Registries.BLOCK.get(identifierIota.identifier))
				else -> null
			} }
		))

		register("moreiotas_itemstack_compat", "adwaqw", HexDir.EAST, OpIotaBijection<ItemStackIota, ram.talia.moreiotas.api.casting.iota.ItemStackIota>("itemstack", ItemStackIota.TYPE, ram.talia.moreiotas.api.casting.iota.ItemStackIota.TYPE,
			{ ItemStackIota.createOptimized(it.itemStack) },
			{ ram.talia.moreiotas.api.casting.iota.ItemStackIota.createFiltered(it.stack) }
		))
	}
}