package microchaos.config

import java.io.InputStream

interface BehaviorConfigSource {
    fun canBeUsed() : Boolean
    fun loadConfiguration(): InputStream;
}
