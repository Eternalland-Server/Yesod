package net.sakuragame.eternal.yesod.function.command

import net.sakuragame.eternal.yesod.mode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.command
import taboolib.expansion.createHelper
import taboolib.module.chat.colored

/**
 * 飞行.
 */
object FunctionFlyCommand {

    init {
        command("fly", permission = "*") {
            createHelper()
            execute<Player> { player, _, _ ->
                player.performCommand("fly ${player.name}")
            }
            dynamic(commit = "player") {
                execute<CommandSender> { sender, _, argument ->
                    val player = getPlayerFromStr(sender, argument) ?: return@execute
                    player.allowFlight = !player.allowFlight
                    sender.sendMessage("&c[System] &7${player.name} 的飞行状态被设定为 ${player.allowFlight.mode()}.".colored())
                }
            }
        }
    }
}