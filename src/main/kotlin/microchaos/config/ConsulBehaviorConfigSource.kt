package microchaos.config

import com.orbitz.consul.Consul
import microchaos.infra.Configuration
import microchaos.infra.logging.loggerFor
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.lang.IllegalStateException


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
        return keyValueClient
            .getValueAsString(Configuration.serviceName)
            .orElseThrow {
                ServiceConfigNotFoundException("Config for service ${Configuration.serviceName} not found.")
            }?.let {
                ByteArrayInputStream(it.toByteArray())
            } ?: throw IllegalStateException("Error while reading config for service ${Configuration.serviceName}")
    }

    override fun onConfigChanged(listener: ConfigChangeListener) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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