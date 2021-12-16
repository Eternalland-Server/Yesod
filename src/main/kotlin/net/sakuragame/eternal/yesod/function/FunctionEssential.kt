package net.sakuragame.eternal.yesod.function

import net.sakuragame.eternal.yesod.Yesod
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.colored

object FunctionEssential {

    /**
     * 牌子颜色
     */
    @SubscribeEvent
    fun e(e: SignChangeEvent) {
        if (e.player.hasPermission("yesod.color")) {
            (0..3).forEach { e.setLine(it, e.getLine(it)?.colored() ?: "") }
        }
    }

    /**
     * 进入提示
     */
    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        val message = Yesod.conf.get("join-message")
        if (message == null || message.toString().isEmpty()) {
            e.joinMessage = null
        } else {
            e.joinMessage = message.toString().colored().replace("@p", e.player.name)
        }
    }

    /**
     * 离开提示
     */
    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        val message = Yesod.conf.get("quit-message")
        if (message == null || message.toString().isEmpty()) {
            e.quitMessage = null
        } else {
            e.quitMessage = message.toString().colored().replace("@p", e.player.name)
        }
    }

    /**
     * 允许玩家喝下带有饱和效果的药水
     */
    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onConsume(e: PlayerItemConsumeEvent) {
        if (e.item.itemMeta is PotionMeta) {
            (e.item.itemMeta as PotionMeta).customEffects.forEach {
                if (it.type == PotionEffectType.SATURATION) {
                    e.player.addPotionEffect(it)
                }
            }
        }
    }
}