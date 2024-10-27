package org.teamvoided.template

import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.slot.SlotBasedPredicate
import io.wispforest.accessories.api.slot.SlotType
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object Template {
    const val MODID = "template"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(Template::class.simpleName)

    fun init() {
        log.info("Hello from Common")
//        AccessoriesAPI.registerPredicate(id("any")
//        ) { level, slotType, slot, stack -> TriState.TRUE }
    }

    fun id(path: String) = Identifier.of(MODID, path)
}
