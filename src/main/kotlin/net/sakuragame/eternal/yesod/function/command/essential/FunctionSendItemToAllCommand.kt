package net.sakuragame.eternal.yesod.function.command.essential

import net.sakuragame.eternal.yesod.function.command.ICommand
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.command.command
import taboolib.module.chat.colored
import taboolib.platform.util.giveItem
import taboolib.platform.util.isAir
import taboolib.platform.util.sendLang

/**
 * 将手上物品发给全服所有玩家.
 */
object FunctionSendItemToAllCommand : ICommand {

    override fun i() {
        command("sendItemToAll", permission = "*") {
            execute<Player> { player, _, _ ->
                val item = player.inventory.itemInMainHand
                if (item.isAir) {
                    player.sendMessage("&c[System] &7不能发给玩家空气.".colored())
                    return@execute
                }
                Bukkit.getOnlinePlayers().forEach {
                    it.giveItem(item)
                    it.sendLang("command-received-item-by-admin", item.itemMeta.displayName, item.amount)
                }
            }
        }
    }
}