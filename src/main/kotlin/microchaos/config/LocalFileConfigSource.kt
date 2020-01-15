package microchaos.config

import microchaos.infra.logging.loggerFor
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class LocalFileConfigSource : ConfigSource {

    companion object {
        const val FILE_PATH_ENV_VAR = "MICROCHAOS_CONFIG_PATH"
        private val logger = loggerFor<LocalFileConfigSource>()
    }

    override fun canBeUsed(): Boolean {
        return System.getenv().containsKey(FILE_PATH_ENV_VAR)
    }

    override fun loadConfiguration(): InputStream {
        val filePath = System.getenv().getOrElse(FILE_PATH_ENV_VAR, {
            throw IllegalArgumentException("Environment variable $FILE_PATH_ENV_VAR not provided")
        })
        try {
            logger.info("Loading configuration from local file: $filePath")
            return FileInputStream(filePath)
        } catch (exc: IOException) {
            val message = "Configuration file not found: '$filePath'"
            throw ServiceConfigNotFoundException(message, exc)
        }
    }

}