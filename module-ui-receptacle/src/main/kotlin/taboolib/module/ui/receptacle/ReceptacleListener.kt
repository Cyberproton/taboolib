package taboolib.module.ui.receptacle

import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.PacketReceiveEvent

@PlatformSide([Platform.BUKKIT])
object ReceptacleListener {

    @SubscribeEvent
    fun e(e: PacketReceiveEvent) {
        val receptacle = e.player.getViewingReceptacle() ?: return
        if (e.packet.name == "PacketPlayInWindowClick") {
            val id = if (MinecraftVersion.isUniversal) {
                e.packet.read<Int>("containerId")
            } else {
                e.packet.read<Int>("a")
            }
            if (id == 119) {
                val slot: Int
                val mode: String
                val button: Int
                if (MinecraftVersion.isUniversal) {
                    slot = e.packet.read<Int>("slotNum")!!
                    mode = e.packet.read<Any>("clickType").toString()
                    button = e.packet.read<Int>("buttonNum")!!
                } else {
                    slot = e.packet.read<Int>("slot")!!
                    mode = e.packet.read<Any>("shift").toString()
                    button = e.packet.read<Int>("button")!!
                }
                val clickType = ReceptacleClickType.from(mode, button, slot) ?: return
                val evt = ReceptacleInteractEvent(e.player, receptacle, clickType, slot)
                evt.call()
                receptacle.callEventClick(evt)
                if (evt.isCancelled) {
                    PacketWindowSetSlot(slot = -1, windowId = -1).send(e.player)
                }
                e.isCancelled = true
            }
        } else if (e.packet.name == "PacketPlayInCloseWindow") {
            val id = if (MinecraftVersion.isUniversal) {
                e.packet.read<Int>("containerId")
            } else {
                e.packet.read<Int>("id")
            }
            if (id == 119) {
                receptacle.close(false)
                // 防止关闭菜单后, 动态标题频率过快出现的卡假容器
                submit(delay = 1, async = true) {
                    val viewingReceptacle = e.player.getViewingReceptacle()
                    if (viewingReceptacle != null) {
                        e.player.updateInventory()
                    }
                }
                submit(delay = 4, async = true) {
                    val viewingReceptacle = e.player.getViewingReceptacle()
                    if (viewingReceptacle == receptacle) {
                        PacketWindowClose().send(e.player)
                    }
                }
            }
            e.isCancelled = true
            ReceptacleCloseEvent(e.player, receptacle).call()
        }
    }
}