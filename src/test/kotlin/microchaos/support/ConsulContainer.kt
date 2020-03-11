package microchaos.support

import org.junit.ClassRule
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class ConsulContainer(dockerImageName: String) : GenericContainer<ConsulContainer>(dockerImageName){

    fun withDefaultConfig(): ConsulContainer {
        return this
            .withExposedPorts(80)
            .withEnv("MAGIC_NUMBER", "42")
            .withCommand(
                "/bin/sh", "-c",
                "test"
            )
    }
    companion object {
        @ClassRule
        @JvmField
        protected val consul = ConsulContainer("consul:latest")
    }

}