package microchaos.config

import java.io.InputStream

interface ConfigSource {
    fun canBeUsed() : Boolean
    fun loadConfiguration(): InputStream;
}
