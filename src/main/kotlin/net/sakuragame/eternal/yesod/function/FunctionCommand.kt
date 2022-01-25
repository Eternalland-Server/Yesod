package net.sakuragame.eternal.yesod.function

import net.sakuragame.eternal.yesod.Yesod
import net.sakuragame.eternal.yesod.mode
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
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
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.platform.BukkitCommand
import taboolib.platform.util.giveItem
import taboolib.platform.util.isAir
import taboolib.platform.util.sendLang


@Suppress("SpellCheckingInspection")
object FunctionCommand {

    val ILLEGAL_ARGUMENTS_MSG = "&c[System] &7非法的参数.".colored()
    val UNKNOWN_PLAYER_MSG = "&c[System] &7玩家不存在.".colored()

    init {
        /**
         * 查看某人的背包.
         */
        command("invsee", permission = "*") {
            dynamic(commit = "player") {
                suggestion<Player> { _, _ ->
                    Bukkit.getOnlinePlayers().map { it.name }
                }
                execute<Player> { sender, _, argument ->
                    val player = Bukkit.getPlayer(argument)
                    if (player == null) {
                        sender.sendMessage(UNKNOWN_PLAYER_MSG)
                        return@execute
                    }
                    sender.openInventory(player.inventory)
                    sender.sendMessage("&c[System] &7您已打开 ${player.name} 的背包.".colored())
                }
            }
        }

        /**
         * 飞行.
         */
        command("fly", permission = "*") {
            createHelper()
            execute<Player> { player, _, _ ->
                player.performCommand("fly ${player.name}")
            }
            dynamic(commit = "player") {
                execute<CommandSender> { sender, _, argument ->
                    val player = Bukkit.getPlayer(argument)
                    if (player == null) {
                        sender.sendMessage(UNKNOWN_PLAYER_MSG)
                        return@execute
                    }
                    player.allowFlight = !player.allowFlight
                    sender.sendMessage("&c[System] &7${player.name} 的飞行状态被设定为 ${player.allowFlight.mode()}.".colored())
                }
            }
        }

        /**
         * 游戏模式.
         */
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
                        val player = Bukkit.getPlayer(context.get(1))
                        if (player == null) {
                            sender.sendMessage(UNKNOWN_PLAYER_MSG)
                            return@execute
                        }
                        val gameMode = GameMode.values().find { it.value == value } ?: GameMode.CREATIVE
                        player.gameMode = gameMode
                        sender.sendMessage("&c[System] &7${player.name} 的游戏模式被设定为 ${player.gameMode.name.uppercase()}.".colored())
                    }
                }
            }
        }

        /**
         * 移动速度.
         */
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
                        player.sendMessage(ILLEGAL_ARGUMENTS_MSG)
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

        /**
         * 将手上物品发给全服所有玩家.
         */
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

        /**
         * 传送到某个坐标.
         */
        command("tppos", permission = "*") {
            createHelper()
            dynamic("x") {
                dynamic("y") {
                    dynamic("z") {
                        execute<Player> { player, context, _ ->
                            player.performCommand("tppos ${context.get(0)} ${context.get(1)} ${context.get(2)} ${player.name}")
                        }
                        dynamic("player") {
                            execute<CommandSender> { sender, context, _ ->
                                val player = Bukkit.getPlayer(context.get(3))
                                if (player == null) {
                                    sender.sendMessage(UNKNOWN_PLAYER_MSG)
                                    return@execute
                                }
                                val x = if (context.get(0) == "~") {
                                    player.location.x
                                } else {
                                    context.get(0).toDoubleOrNull() ?: kotlin.run {
                                        sender.sendMessage(ILLEGAL_ARGUMENTS_MSG)
                                        return@execute
                                    }
                                }
                                val y = if (context.get(1) == "~") {
                                    player.location.x
                                } else {
                                    context.get(1).toDoubleOrNull() ?: kotlin.run {
                                        sender.sendMessage(ILLEGAL_ARGUMENTS_MSG)
                                        return@execute
                                    }
                                }
                                val z = if (context.get(2) == "~") {
                                    player.location.x
                                } else {
                                    context.get(2).toDoubleOrNull() ?: kotlin.run {
                                        sender.sendMessage(ILLEGAL_ARGUMENTS_MSG)
                                        return@execute
                                    }
                                }
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