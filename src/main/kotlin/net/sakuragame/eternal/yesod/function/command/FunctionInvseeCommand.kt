package net.sakuragame.eternal.yesod.function.command

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.command.command
import taboolib.module.chat.colored


/**
 * 查看某人的背包.
 */
@Suppress("SpellCheckingInspection")
object FunctionInvseeCommand {

    init {
        command("invsee", permission = "*") {
            dynamic(commit = "player") {
                suggestion<Player> { _, _ ->
                    Bukkit.getOnlinePlayers().map { it.name }
                }
                execute<Player> { sender, _, argument ->
                    val player = getPlayerFromStr(sender, argument) ?: return@execute
                    sender.openInventory(player.inventory)
                    sender.sendMessage("&c[System] &7您已打开 ${player.name} 的背包.".colored())
                }
            }
        }
    }
}