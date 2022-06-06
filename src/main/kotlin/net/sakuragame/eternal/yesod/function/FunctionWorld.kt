package net.sakuragame.eternal.yesod.function

import net.sakuragame.eternal.yesod.Yesod
import net.sakuragame.eternal.yesod.Yesod.bypass
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Hanging
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityBreedEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.EntityInteractEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.player.*
import org.bukkit.util.Vector
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

object FunctionWorld {

    @SubscribeEvent
    fun e(e: BlockPlaceEvent) {
        if (e.player.bypass()) {
            return
        }
        e.isCancelled = true
    }

    @SubscribeEvent
    fun e(e: PlayerFishEvent) {
        e.isCancelled = true
    }

    @SubscribeEvent
    fun e(e: BlockBreakEvent) {
        if (e.player.bypass()) {
            return
        }
        e.isCancelled = true
    }

    @SubscribeEvent
    fun e(e: BlockPhysicsEvent) {
        if (Yesod.disableBlockPhysical) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: BlockSpreadEvent) {
        if (Yesod.disableBlockPhysical) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: BlockFromToEvent) {
        if (Yesod.disableBlockPhysical) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerRespawnEvent) {
        e.respawnLocation = e.respawnLocation.world!!.spawnLocation
    }

    @SubscribeEvent
    fun e(e: EntityBreedEvent) {
        if ("BREED" in Yesod.blockFeatures) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: LeavesDecayEvent) {
        if ("LEAVES_DECAY" in Yesod.blockFeatures) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: EntityChangeBlockEvent) {
        if ("ENTITY_CHANGE_BLOCK" in Yesod.blockFeatures && e.entity is LivingEntity) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerInteractEvent) {
        if ("FARMLAND_PHYSICAL" in Yesod.blockFeatures && e.action == Action.PHYSICAL && e.clickedBlock!!.type == Material.SOIL) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: EntityInteractEvent) {
        if ("FARMLAND_PHYSICAL" in Yesod.blockFeatures && e.block.type == Material.SOIL) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: HangingBreakEvent) {
        if ("HANGING_BREAK" in Yesod.blockFeatures && e.cause != HangingBreakEvent.RemoveCause.ENTITY) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: HangingBreakByEntityEvent) {
        if ("HANGING_BREAK" in Yesod.blockFeatures && e.remover?.bypass(true) == false) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerInteractEntityEvent) {
        if ("HANGING_BREAK" in Yesod.blockFeatures && !e.player.bypass(true) && e.rightClicked is Hanging) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerTeleportEvent) {
        e.isCancelled = e.cause.name in Yesod.blockTeleport
    }

    @SubscribeEvent
    fun e(e: EntityExplodeEvent) {
        if ("ENTITY_EXPLODE" in Yesod.blockFeatures) {
            e.blockList().clear()
        }
    }

    @SubscribeEvent
    fun e(e: BlockExplodeEvent) {
        if ("BLOCK_EXPLODE" in Yesod.blockFeatures) {
            e.blockList().clear()
        }
    }

    @SubscribeEvent
    fun e(e: PlayerMoveEvent) {
        val to = e.to!!
        if (e.from.x != to.x || e.from.y != to.y || e.from.z != to.z) {
            if (to.y < 0 && Yesod.voidProtect && e.player.gameMode != GameMode.CREATIVE) {
                e.isCancelled = true
                // 返回大厅
                submit {
                    e.player.velocity = Vector(0, 0, 0)
                    e.player.teleport(Yesod.multiverseCore.mvWorldManager.getMVWorld(e.player.world).spawnLocation)
                }
            }
        }
    }
}