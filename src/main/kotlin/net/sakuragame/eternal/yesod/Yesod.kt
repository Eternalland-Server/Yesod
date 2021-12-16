package net.sakuragame.eternal.yesod

import com.onarandombox.MultiverseCore.MultiverseCore
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.generator.ChunkGenerator
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitWorldGenerator

object Yesod : Plugin(), BukkitWorldGenerator {

    @Config
    lateinit var conf: Configuration
        private set

    @ConfigNode("void-protect")
    var voidProtect = false
        private set

    @ConfigNode("allow-craft")
    var allowCraft = false
        private set

    @ConfigNode("allow-craft-display")
    var allowCraftDisplay = false
        private set

    @ConfigNode("block-inventory")
    lateinit var blockInventory: List<String>
        private set

    @ConfigNode("block-interact")
    lateinit var blockInteract: List<String>
        private set

    @ConfigNode("thorn-override")
    var thornOverride = false
        private set

    @ConfigNode("block-features")
    lateinit var blockFeatures: List<String>
        private set

    @ConfigNode("block-teleport")
    lateinit var blockTeleport: List<String>
        private set

    val multiverseCore by lazy {
        Bukkit.getServer().pluginManager.getPlugin("Multiverse-Core") as MultiverseCore
    }

    fun Entity.bypass(hard: Boolean = false): Boolean {
        return this !is Player || isOp && gameMode == GameMode.CREATIVE && (!hard || isSneaking)
    }

    override fun getDefaultWorldGenerator(worldName: String, name: String?): ChunkGenerator {
        return YesodGenerator()
    }

    override fun onActive() {
        Bukkit.getWorlds().forEach {
            it.setGameRuleValue("announceAdvancements", "false")
            it.setGameRuleValue("keepInventory", "true")
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("showDeathMessages", "false")
        }
    }
}