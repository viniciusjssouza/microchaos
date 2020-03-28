package microchaos.config

import java.io.InputStream

typealias ConfigChangeListener = (newConfig: InputStream) -> Unit

interface BehaviorConfigSource {
    fun canBeUsed() : Boolean
    fun loadConfiguration(): InputStream
    fun onConfigChanged(listener: ConfigChangeListener)
}
