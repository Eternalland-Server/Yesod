package net.sakuragame.eternal.yesod.function

import net.sakuragame.eternal.yesod.Yesod
import org.bukkit.inventory.CraftingInventory
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketReceiveEvent

object FunctionPacket {

    @SubscribeEvent
    fun e(e: PacketReceiveEvent) {
        if (e.packet.name == "PacketPlayInAutoRecipe" || e.packet.name == "PacketPlayInRecipeDisplayed") {
            if (!Yesod.allowCraftDisplay) {
                e.isCancelled = true
            }
        }
        if (e.packet.name == "PacketPlayInUseItem" || e.packet.name == "PacketPlayInUseEntity" || e.packet.name == "PacketPlayInArmAnimation") {
            if (e.player.openInventory.topInventory !is CraftingInventory) {
                e.isCancelled = true
            }
        }
    }
}