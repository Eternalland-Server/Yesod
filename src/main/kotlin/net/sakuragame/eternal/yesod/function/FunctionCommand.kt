package net.sakuragame.eternal.yesod.function

import net.sakuragame.eternal.yesod.Yesod
import net.sakuragame.eternal.yesod.mode
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.spigotmc.SpigotConfig
import taboolib.common.LifeCycle
import taboolib.common.io.taboolibId
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.command.command
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.BukkitCommand

@Suppress("SpellCheckingInspection")
object FunctionCommand {

    init {
        command("fly") {
            execute<Player> { player, _, _ ->
                if (!player.hasPermission("yesod.fly")) {
                    player.sendMessage(SpigotConfig.unknownCommandMessage)
                    return@execute
                }
                player.allowFlight = !player.allowFlight
                player.sendMessage("您设置了您的飞行状态为${player.allowFlight.mode()}")
            }
        }
        command("speed") {
            dynamic {
                execute<Player> { player, _, argument ->
                    if (!player.hasPermission("yesod.speed")) {
                        player.sendMessage(SpigotConfig.unknownCommandMessage)
                        return@execute
                    }
                    val speed = argument.toFloatOrNull()
                    if (speed == null) {
                        player.sendMessage("您输入的不是一个合法的值.")
                        return@execute
                    }
                    if (speed > 1.0) {
                        player.sendMessage("您输入的值太高了!")
                        return@execute
                    }
                    player.walkSpeed = speed
                    player.flySpeed = speed
                    player.sendMessage("您设置了您的速度值为: $speed")
                }
            }
        }
        command("tppos") {
            dynamic("x") {
                dynamic("y") {
                    dynamic("z") {
                        execute<Player> { sender, context, _ ->
                            if (!sender.hasPermission("yesod.tppos")) {
                                sender.sendMessage(SpigotConfig.unknownCommandMessage)
                                return@execute
                            }
                            val x = if (context.get(0) == "~") {
                                sender.location.x
                            } else {
                                context.get(0).toDoubleOrNull() ?: return@execute
                            }
                            val y = if (context.get(1) == "~") {
                                sender.location.x
                            } else {
                                context.get(0).toDoubleOrNull() ?: return@execute
                            }
                            val z = if (context.get(2) == "~") {
                                sender.location.x
                            } else {
                                context.get(0).toDoubleOrNull() ?: return@execute
                            }
                            sender.teleport(sender.location.also {
                                it.x = x
                                it.y = y
                                it.z = z
                            })
                            sender.sendMessage("您传送到了坐标: ${sender.location.toVector()}")
                        }
                    }
                }
            }
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun e() {
        PlatformFactory.getAPI<BukkitCommand>().commandMap.commands.forEach { command ->
            if (Yesod.conf.getStringList("block-command-path").any { name -> command.javaClass.name.startsWith(name) }) {
                if (command !is PluginCommand || !command.javaClass.name.startsWith("io.izzel.$taboolibId")) {
                    command.permission = "*"
                }
            }
        }


        @SubscribeEvent
        fun e(e: PlayerCommandPreprocessEvent) {
            if (e.player.isOp) {
                return
            }
            val v = e.message.split(" ")[0].toLowerCase().substring(1)
            if (v.contains(":") || v in Yesod.conf.getStringList("block-command-name")) {
                e.isCancelled = true
                e.player.sendMessage(SpigotConfig.unknownCommandMessage)
            }
        }
    }
}