package net.sakuragame.eternal.yesod.function.command

import net.sakuragame.eternal.yesod.function.FunctionCommand
import org.bukkit.entity.Player
import org.spigotmc.SpigotConfig
import taboolib.common.platform.command.command
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.platform.util.sendLang

/**
 * 移动速度.
 */
object FunctionMoveSpeed {

    init {
        command("speed", permission = "*") {
            createHelper()
            dynamic {
                execute<Player> { player, _, argument ->
                    if (!player.hasPermission("yesod.speed")) {
                        player.sendMessage(SpigotConfig.unknownCommandMessage)
                        return@execute
                    }
                    val speed = argument.toFloatOrNull()
                    if (speed == null) {
                        player.sendLang("command-illegal-arguments")
                        return@execute
                    }
                    if (speed > 1.0) {
                        player.sendMessage("&c[System] &7您输入的值太高了!".colored())
                        return@execute
                    }
                    player.walkSpeed = speed
                    player.flySpeed = speed
                    player.sendMessage("&c[System] &7您设置了速度为: $speed.".colored())
                }
            }
        }
    }
}