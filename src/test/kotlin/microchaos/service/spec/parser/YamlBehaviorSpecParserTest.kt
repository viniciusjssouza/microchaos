package microchaos.service.spec.parser

import microchaos.service.spec.SampleServices
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class YamlBehaviorSpecParserTest {

    private val parser = YamlBehaviorSpecParser()

    @Test
    fun parseSimpleServiceTest() {
        val input = readYamlFile("/simpleService.yaml");
        val service = parser.parse(input)
        assertThat(service).isEqualTo(SampleServices.simpleService)
    }

    @Test
    fun parsePeriodicTasksTest() {
        val input = readYamlFile("/periodicTasks.yaml");
        val service = parser.parse(input)
        assertThat(service).isEqualTo(SampleServices.periodicTasks)
    }

    private fun readYamlFile(fileName: String): String {
        return this::class.java.getResource(fileName).readText();
    }
}