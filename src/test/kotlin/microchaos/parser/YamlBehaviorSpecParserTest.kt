package microchaos.parser

import microchaos.model.ktor.KtorImplFactory
import microchaos.model.ktor.KtorService
import microchaos.model.ktor.KtorEndpoint
import microchaos.model.SampleServices
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class YamlBehaviorSpecParserTest {

    private val parser = YamlBehaviorSpecParser()

    @Test
    fun `parse a complete simple service`() {
        val input = readYamlFile("/simpleService.yaml")
        val service = parser.parse(input)
        assertThat(service).isEqualTo(SampleServices.simpleService)
    }

    @Test
    fun `parse periodic tasks yaml`() {
        val input = readYamlFile("/periodicTasks.yaml")
        val service = parser.parse(input)
        assertThat(service).isEqualTo(SampleServices.periodicTasks)
    }

    @Test
    fun `should call the provided factory to map the types`() {
        val yamlParser = YamlBehaviorSpecParser(KtorImplFactory())
        val input = readYamlFile("/simpleService.yaml")
        val model = yamlParser.parse(input)

        assertThat(model.service).isInstanceOf(KtorService::class.java)
        assertThat(model.service.endpoints.first()).isInstanceOf(KtorEndpoint::class.java)
    }

    private fun readYamlFile(fileName: String): String {
        return this::class.java.getResource(fileName).readText()
    }
}