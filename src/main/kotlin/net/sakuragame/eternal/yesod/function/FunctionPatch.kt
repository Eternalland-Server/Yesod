package net.sakuragame.eternal.yesod.function

import net.sakuragame.eternal.yesod.Yesod
import net.sakuragame.eternal.yesod.Yesod.bypass
import org.bukkit.Bukkit
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Arrow
import org.bukkit.entity.FishHook
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object FunctionPatch {

    /**
     * 禁止合成物品
     */
    @SubscribeEvent
    fun e(e: CraftItemEvent) {
        if (!Yesod.allowCraft) {
            e.isCancelled = true
        }
    }

    /**
     * 禁止合成
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun e(e: InventoryClickEvent) {
        if (!Yesod.allowCraft) {
            Bukkit.broadcastMessage("reached 1")
            e.isCancelled = e.clickedInventory?.type == InventoryType.CRAFTING
        }
    }

    /**
     * 禁止合成
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun e(e: InventoryDragEvent) {
        if (!Yesod.allowCraft) {
            e.isCancelled = e.inventory.type == InventoryType.CRAFTING && e.rawSlots.any { it < 5 }
        }
    }

    /**
     * 禁止打开界面
     */
    @SubscribeEvent
    fun e(e: InventoryOpenEvent) {
        e.isCancelled = e.inventory.type.name in Yesod.blockInventory
    }

    /**
     * 禁止方块交互
     */
    @SubscribeEvent
    fun e(e: PlayerInteractEvent) {
        if ((e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.LEFT_CLICK_BLOCK) && !e.player.bypass()) {
            val type = e.clickedBlock!!.type.name
            if (Yesod.blockInteract.any { if (it.endsWith("?")) it.substring(0, it.length - 1) in type else it == type }) {
                e.isCancelled = true
            }
        }
    }

    /**
     * 禁止鱼竿移动公民
     * 禁止创造模式射出的弓箭在世界停留
     */
    @SubscribeEvent
    fun e(e: ProjectileHitEvent) {
        if (e.entity is FishHook && (e.hitEntity is ArmorStand || e.hitEntity?.hasMetadata("NPC") == true)) {
            e.entity.remove()
        }
        if (e.entity is Arrow && (e.entity as Arrow).pickupStatus != Arrow.PickupStatus.ALLOWED) {
            e.entity.remove()
        }
    }

    fun getArmor(entity: LivingEntity): Array<ItemStack?> {
        val items = arrayOfNulls<ItemStack>(6)
        items[0] = entity.equipment?.helmet
        items[1] = entity.equipment?.chestplate
        items[2] = entity.equipment?.leggings
        items[3] = entity.equipment?.boots
        return items
    }
}