package microchaos.support

import microchaos.infra.logging.loggerFor
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer

open class ConsulContainer(dockerImageName: String) : GenericContainer<ConsulContainer>(dockerImageName) {

    companion object {
        private val log = loggerFor<ConsulContainer>()
    }

    fun withDefaultConfig(): ConsulContainer {
        return this
            .withNetworkMode("host")
            .withCommand("consul agent -server -bind=0.0.0.0 -advertise=127.0.0.1 -bootstrap -data-dir=/consul/data")
            .withLogConsumer(Slf4jLogConsumer(log))
    }


}