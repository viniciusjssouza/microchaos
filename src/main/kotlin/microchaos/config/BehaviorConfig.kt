package microchaos.config

import microchaos.infra.logging.loggerFor
import microchaos.model.ServiceModel
import microchaos.parser.ServiceSpecParser
import java.io.InputStream

typealias ServiceModelChangeListener = (model: ServiceModel) -> Unit

class BehaviorConfig(private val configSources: Array<BehaviorConfigSource>, private val parser: ServiceSpecParser) {

    companion object {
        private val log = loggerFor<BehaviorConfig>()
    }

    fun load(): ServiceModel {
        val configSource = this.getAvailableConfigSource()
        log.info("Using config source '${configSource.javaClass.name}'")
        val inputStream = configSource.loadConfiguration()
        return this.read(inputStream)
    }

    fun onModelChange(listener: ServiceModelChangeListener): BehaviorConfig {
        val configSource = this.getAvailableConfigSource()
        configSource.onConfigChanged { newConfig: InputStream ->
            val model = this.read(newConfig)
            listener(model)
        }
        return this
    }

    private fun read(inputStream: InputStream): ServiceModel {
        val allLines = inputStream.bufferedReader().use {
            it.readText()
        }
        return parser.parse(allLines)
    }

    private fun getAvailableConfigSource(): BehaviorConfigSource {
        return configSources.find(BehaviorConfigSource::canBeUsed)
            ?: throw IllegalArgumentException(
                "No usable config source found. Options searched: ${this.getSourceNames()}"
            )
    }

    private fun getSourceNames(): List<String> {
        return configSources.map { it.javaClass.name }
    }
}