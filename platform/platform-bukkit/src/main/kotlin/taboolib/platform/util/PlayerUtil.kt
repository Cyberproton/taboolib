@file:Isolated

package taboolib.platform.util

import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack
import taboolib.common.Isolated
import taboolib.common.platform.function.adaptPlayer

fun HumanEntity.giveItem(itemStack: List<ItemStack>) {
    itemStack.forEach { giveItem(it) }
}

fun HumanEntity.giveItem(itemStack: ItemStack?, repeat: Int = 1) {
    if (itemStack.isNotAir()) {
        // CraftInventory.addItem 的执行过程中, 实质上有可能修改ItemStack的amount, 如果不注意这一点, 则会吞物品而不自知
        val preAmount = itemStack.amount
        repeat(repeat) {
            inventory.addItem(itemStack).values.forEach { world.dropItem(location, it) }
            itemStack.amount = preAmount
        }
    }
}

fun HumanEntity.getUsingItem(material: Material): ItemStack? {
    return when {
        inventory.itemInMainHand.type == material -> inventory.itemInMainHand
        inventory.itemInOffHand.type == material -> inventory.itemInOffHand
        else -> null
    }
}

fun HumanEntity.sendActionBar(message: String) {
    adaptPlayer(this).sendActionBar(message)
}

fun HumanEntity.actionBar(message: String) {
    adaptPlayer(this).sendActionBar(message)
}

fun HumanEntity.title(title: String?, subTitle: String?) {
    adaptPlayer(this).sendTitle(title, subTitle, 10, 60, 10)
}

fun HumanEntity.title(title: String?, subTitle: String?, fadeIn: Int, stay: Int, fadeOut: Int) {
    adaptPlayer(this).sendTitle(title, subTitle, fadeIn, stay, fadeOut)
}

fun HumanEntity.feed() {
    foodLevel = 20
}

fun HumanEntity.saturate() {
    saturation = 20F
}
