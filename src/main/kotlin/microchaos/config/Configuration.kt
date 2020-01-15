package microchaos.config

import microchaos.model.ServiceModel
import microchaos.parser.ServiceSpecParser
import java.lang.IllegalArgumentException

class Configuration(private val configSources: Array<ConfigSource>, private val parser: ServiceSpecParser) {

    fun load(): ServiceModel {
        configSources.find(ConfigSource::canBeUsed)?.let { configSource ->
            val inputStream = configSource.loadConfiguration()
            val allLines = inputStream.bufferedReader().use {
                it.readText()
            }
            return parser.parse(allLines)
        } ?: throw IllegalArgumentException("No usable config source found. Options searched: ${this.getSourceNames()}")
    }

    private fun getSourceNames(): List<String> {
        return configSources.map { it.javaClass.name }
    }
}