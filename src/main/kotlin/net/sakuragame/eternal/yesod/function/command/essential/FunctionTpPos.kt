package net.sakuragame.eternal.yesod.function.command.essential

import net.sakuragame.eternal.yesod.function.command.ICommand
import net.sakuragame.eternal.yesod.function.command.getPlayerFromStr
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.command
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.platform.util.sendLang

/**
 * 传送到某个坐标.
 */
object FunctionTpPos : ICommand {

    override fun i() {
        @Suppress("SpellCheckingInspection") command("tppos", permission = "*") {
            createHelper()
            dynamic("x") {
                dynamic("y") {
                    dynamic("z") {
                        execute<Player> { player, context, _ ->
                            player.performCommand("tppos ${context.get(0)} ${context.get(1)} ${context.get(2)} ${player.name}")
                        }
                        dynamic("player") {
                            execute<CommandSender> { sender, context, _ ->
                                val player = getPlayerFromStr(sender, context.get(3)) ?: return@execute
                                val x = readPos(sender, context.get(0)) ?: return@execute
                                val y = readPos(sender, context.get(1)) ?: return@execute
                                val z = readPos(sender, context.get(2)) ?: return@execute
                                player.teleport(player.location.also {
                                    it.x = x
                                    it.y = y
                                    it.z = z
                                })
                                player.sendMessage("&c[System] &7您被传送到坐标: ${player.location.toVector()}".colored())
                                if (sender != player) {
                                    sender.sendMessage("&c[System] &7${player.name} 被传送到坐标: ${player.location.toVector()}".colored())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun readPos(sender: CommandSender, str: String): Double? {
        val pos = str.toDoubleOrNull()
        if (pos == null) {
            sender.sendLang("command-illegal-arguments")
            return null
        }
        return pos
    }
}