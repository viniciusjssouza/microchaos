package microchaos.config

import com.orbitz.consul.Consul
import microchaos.infra.Configuration
import java.io.InputStream


class ConsulBehaviorConfigSource : BehaviorConfigSource {

    override fun canBeUsed(): Boolean {
        val hasServiceName = Configuration.serviceName?.isNotBlank() ?: false
        val hasAddress = Configuration.consulUrl?.isNotBlank() ?: false
        return hasServiceName && hasAddress
    }

    override fun loadConfiguration(): InputStream {
//        val client = Consul.builder()
//            .withHostAndPort()
//            .build()
        throw NotImplementedError()
    }
}