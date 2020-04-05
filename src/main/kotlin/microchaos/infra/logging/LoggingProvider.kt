package microchaos.infra.logging

import org.slf4j.LoggerFactory

inline fun <reified T : Any> loggerFor(): org.slf4j.Logger = LoggerFactory.getLogger(T::class.java)
fun loggerFor(name: String): org.slf4j.Logger = LoggerFactory.getLogger(name)

fun org.slf4j.Logger.debug(messageFactory: () -> String) {
    if (this.isDebugEnabled) {
        this.debug(messageFactory())
    }
}



