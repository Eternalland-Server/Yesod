package net.sakuragame.eternal.yesod.function.command

import net.sakuragame.eternal.yesod.function.FunctionCommand
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.command
import taboolib.expansion.createHelper
import taboolib.module.chat.colored

/**
 * 游戏模式.
 */
@Suppress("SpellCheckingInspection")
object FunctionGamemodeCommand {

    init {
        command("gamemode", aliases = listOf("gm"), permission = "*") {
            createHelper()
            dynamic(commit = "type") {
                suggestion<CommandSender> { _, _ ->
                    listOf("0", "1", "2", "3")
                }
                execute<Player> { player, _, argument ->
                    player.performCommand("gameMode $argument ${player.name}")
                }
                dynamic(commit = "player") {
                    suggestion<CommandSender> { _, _ ->
                        Bukkit.getOnlinePlayers().map { it.name }
                    }
                    execute<CommandSender> { sender, context, _ ->
                        val value = context.get(0).toIntOrNull() ?: 1
                        val player = getPlayerFromStr(sender, context.get(1)) ?: return@execute
                        val gameMode = GameMode.values().find { it.value == value } ?: GameMode.CREATIVE
                        player.gameMode = gameMode
                        sender.sendMessage("&c[System] &7${player.name} 的游戏模式被设定为 ${player.gameMode.name.uppercase()}.".colored())
                    }
                }
            }
        }
    }
}