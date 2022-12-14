package net.sakuragame.eternal.yesod.function

import net.sakuragame.eternal.yesod.Yesod
import net.sakuragame.eternal.yesod.function.command.ICommand
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI
import org.bukkit.command.PluginCommand
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.reflections.Reflections
import org.spigotmc.SpigotConfig
import taboolib.common.LifeCycle
import taboolib.common.io.getInstance
import taboolib.common.io.taboolibId
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.BukkitCommand


@Suppress("SpellCheckingInspection")
object FunctionCommand {

    @Awake(LifeCycle.ENABLE)
    fun i() {
        val ref = Reflections("net.sakuragame.eternal.yesod.function.command")
        val classes = ref.getSubTypesOf(ICommand::class.java)
        classes.mapNotNull { it.getInstance(false)?.get() }.forEach {
            it.i()
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun e() {
        if (ClientManagerAPI.getServerID().lowercase().contains("rpg-login")) {
            return
        }
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