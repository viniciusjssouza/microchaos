package microchaos.config

import io.mockk.every
import io.mockk.mockkObject
import microchaos.infra.Configuration
import microchaos.model.SampleServices
import microchaos.parser.YamlBehaviorSpecParser
import microchaos.support.ConsulContainer
import microchaos.support.ModelFiles
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.FileReader

@Testcontainers
internal class ConsulBehaviorConfigSourceIT {

    companion object {
        const val serviceName = "testService"

        @Container
        @JvmStatic
        val consulContainer = ConsulContainer("consul:latest").withDefaultConfig()

        @BeforeAll
        @JvmStatic
        fun setupConfiguration() {
            mockkObject(Configuration)
            val consulUrl = "http://${consulContainer.containerIpAddress}:8500"
            every { Configuration.consulUrl } returns consulUrl
            every { Configuration.serviceName } returns serviceName
        }
    }

    private val configSource = ConsulBehaviorConfigSource()

    @Test
    fun `load service config`() {
        configSource.putConfiguration(serviceName, FileReader(ModelFiles.simpleService()).readText())
        val modelRepresentation = configSource.loadConfiguration().bufferedReader().readText()
        val parsedModel = YamlBehaviorSpecParser().parse(modelRepresentation)

        assertThat(parsedModel).isEqualTo(SampleServices.simpleService)
    }

    @Test
    fun `reload service config`() {
        configSource.putConfiguration(serviceName, "Before change")
        var newText = ""
        Thread.sleep(50)
        configSource.onConfigChanged {
            newText = it.bufferedReader().readText()
        }
        configSource.putConfiguration(serviceName, "After change")
        Thread.sleep(50)
        assertThat(newText).isEqualTo("After change")
    }
}