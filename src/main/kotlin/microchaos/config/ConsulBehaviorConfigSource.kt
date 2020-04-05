package microchaos.config

import com.orbitz.consul.Consul
import com.orbitz.consul.cache.KVCache
import com.orbitz.consul.model.kv.Value
import microchaos.infra.Configuration
import microchaos.infra.logging.loggerFor
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*


class ConsulBehaviorConfigSource : BehaviorConfigSource {

    companion object {
        private val log = loggerFor<ConsulBehaviorConfigSource>()
    }

    private val client: Consul by lazy {
        this.createClient()
    }

    override fun canBeUsed(): Boolean {
        val hasServiceName = Configuration.serviceName?.isNotBlank() ?: false
        val hasAddress = Configuration.consulUrl?.isNotBlank() ?: false
        return hasServiceName && hasAddress
    }

    override fun loadConfiguration(): InputStream {
        val keyValueClient = client.keyValueClient()
        val consulValue = keyValueClient.getValue(Configuration.serviceName)
        return this.readValue(consulValue)
    }

    override fun onConfigChanged(listener: ConfigChangeListener) {
        val kvClient = client.keyValueClient()
        val cache = KVCache.newCache(kvClient, Configuration.serviceName)
        cache.addListener { newValues ->
            log.info("Reloading config for service '${Configuration.serviceName}'")
            val newValue = newValues.values
                .stream()
                .filter { value -> value.key == Configuration.serviceName }
                .findFirst()
            val inputStream = this.readValue(newValue)
            listener(inputStream)
            log.info("Config for service '${Configuration.serviceName}' reloaded")
        }
        cache.start()
        log.info("Watching for changes on key '${Configuration.serviceName}'")
    }

    private fun readValue(consulValue: Optional<Value?>): InputStream {
        return consulValue
            .orElseThrow {
                ServiceConfigNotFoundException("Config for service '${Configuration.serviceName}' not found.")
            }?.let {
                it.valueAsString.orElseThrow {
                    IllegalStateException("Config value not found for service '${Configuration.serviceName}'.")
                }
            }?.let {
                ByteArrayInputStream(it.toByteArray())
            } ?: throw IllegalStateException("Error while reading config for service ${Configuration.serviceName}")
    }

    fun putConfiguration(serviceName: String, config: String) {
        val keyValueClient = client.keyValueClient()
        keyValueClient.putValue(serviceName, config)
    }

    private fun createClient(): Consul {
        log.info("Initializing consul client to url '${Configuration.consulUrl}'")
        return Consul.builder()
            .withUrl(Configuration.consulUrl)
            .build()
    }
}