package microchaos.config

import io.mockk.every
import io.mockk.mockkObject
import microchaos.infra.Configuration
import microchaos.model.SampleServices
import microchaos.parser.YamlBehaviorSpecParser
import microchaos.support.ConsulContainer
import microchaos.support.ModelFiles
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.FileReader

@Testcontainers
internal class ConsulBehaviorConfigSourceIT {

    @Container
    val consulContainer = ConsulContainer("consul:latest").withDefaultConfig()

    private val configSource = ConsulBehaviorConfigSource()
    private val serviceName = "testService"


    @BeforeEach
    fun setupConfiguration() {
        mockkObject(Configuration)
        val consulUrl = "http://localhost:8500"
        every { Configuration.consulUrl } returns consulUrl
        every { Configuration.serviceName } returns serviceName
        configSource.putConfiguration(serviceName, FileReader(ModelFiles.simpleService()).readText())
    }

    @Test
    fun `load service config`() {
        val modelRepresentation = configSource.loadConfiguration().bufferedReader().readText()
        val parsedModel = YamlBehaviorSpecParser().parse(modelRepresentation)

        assertThat(parsedModel).isEqualTo(SampleServices.simpleService)
    }
}