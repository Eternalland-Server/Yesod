package net.sakuragame.eternal.yesod.function.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.platform.util.sendLang

fun getPlayerFromStr(sender: CommandSender, str: String): Player? {
    val player = Bukkit.getPlayer(str)
    if (player == null) {
        sender.sendLang("command-unknown-player")
        return null
    }
    return player
}