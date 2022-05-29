package net.sakuragame.eternal.yesod.function.command

import com.okkero.skedule.schedule
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.event.player.PlayerLoginEvent
import taboolib.common.platform.command.command
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.colored
import taboolib.platform.util.onlinePlayers
import taboolib.platform.util.title

object FunctionRestartCommand : ICommand {

    var restarting = false

    @SubscribeEvent
    fun e(e: PlayerLoginEvent) {
        if (restarting) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Bukkit.getShutdownMessage())
        }
    }

    override fun i() {
        command("restart", permission = "admin") {
            execute<CommandSender> { sender, _, _ ->
                Bukkit.dispatchCommand(sender, "restart 10")
            }
            dynamic("secs") {
                execute<CommandSender> { sender, _, argument ->
                    val secs = argument.toIntOrNull()
                    if (secs == null) {
                        sender.sendMessage("&c[System] &7格式错误.")
                        return@execute
                    }
                    restart(secs)
                }
            }
        }
    }

    @Suppress("KotlinConstantConditions")
    private fun restart(i: Int) {
        var secs = i
        Bukkit.getScheduler().schedule {
            onlinePlayers.forEach {
                it.playSound(it.location, Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 1f, 1.5f)
                it.title("", "&7&o服务器将在 $secs 秒后关闭".colored(), 5, 25, 0)
            }
            waitFor(20)
            do {
                secs--
                onlinePlayers.forEach {
                    it.playSound(it.location, Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1f, 1.5f)
                    if (secs <= 5 || secs % 5 == 0) {
                        it.title("", "&7&o服务器将在 $secs 秒后关闭".colored(), 0, 25, 0)
                    }
                }
                waitFor(20)
            } while (secs > 0)
            restarting = true
            onlinePlayers.forEach {
                it.kickPlayer(Bukkit.getShutdownMessage())
            }
            waitFor(60)
            Bukkit.shutdown()
        }
    }
}