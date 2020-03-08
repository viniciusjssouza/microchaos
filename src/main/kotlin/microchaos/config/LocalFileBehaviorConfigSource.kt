package microchaos.config

import microchaos.infra.Configuration
import microchaos.infra.logging.loggerFor
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class LocalFileBehaviorConfigSource : BehaviorConfigSource {

    companion object {
        private val logger = loggerFor<LocalFileBehaviorConfigSource>()
    }

    override fun canBeUsed(): Boolean {
        return Configuration.localFileConfigPath?.isNotBlank() ?: false
    }

    override fun loadConfiguration(): InputStream {
        val filePath = Configuration.localFileConfigPath
            ?: throw IllegalArgumentException("Local file path configuration not found")
        try {
            logger.info("Loading configuration from local file: $filePath")
            return FileInputStream(filePath)
        } catch (exc: IOException) {
            val message = "Configuration file not found: '$filePath'"
            throw ServiceConfigNotFoundException(message, exc)
        }
    }

}