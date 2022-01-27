package taboolib.common.io

import taboolib.common.InstGetter
import taboolib.common.platform.PlatformFactory
import taboolib.internal.InstGetterException
import taboolib.internal.InstGetterInstant
import taboolib.internal.InstGetterLazy

/**
 * 取该类在当前项目中被加载的任何实例
 * 例如：@Awake 自唤醒类，或是 Kotlin Companion Object、Kotlin Object 对象
 * @param newInstance 若无任何已加载的实例，是否实例化
 */
fun <T> Class<T>.findInstance(newInstance: Boolean = false): InstGetter<T> {
    try {
        val awoken = PlatformFactory.INSTANCE.getAwakeInstance(this)
        if (awoken != null) {
            return InstGetterInstant(this, awoken)
        }
    } catch (ex: Throwable) {
        return InstGetterException(this, ex)
    }
    return InstGetterLazy.of(this, newInstance)
}

/**
 * 根据当前运行平台获取给定类的实例
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<T>.findInstanceFromPlatform(): T? {
    return runningClasses.firstOrNull { isAssignableFrom(it) && it != this && PlatformFactory.INSTANCE.checkPlatform(it) }?.findInstance(true) as T?
}