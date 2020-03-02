package microchaos.config

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import microchaos.model.SampleServices
import microchaos.parser.ServiceSpecParser
import microchaos.support.ModelFiles
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.FileInputStream

internal class ConfigurationTest {

    class NotApplicableConfigSource : ConfigSource {
        override fun canBeUsed() = false
        override fun loadConfiguration() = throw UnsupportedOperationException()
    }

    class LocalFileConfigSource : ConfigSource {
        override fun canBeUsed() = true
        override fun loadConfiguration() = FileInputStream(ModelFiles.simpleService())
    }

    private val parser = mockk<ServiceSpecParser>()

    @Test
    fun `when no config source can be used`() {
        val config = Configuration(
            arrayOf(
                NotApplicableConfigSource()
            ),
            parser
        )
        assertThrows(IllegalArgumentException::class.java) { config.load() }
    }

    @Test
    fun `when a config source is used`() {
        val config = Configuration(
            arrayOf(
                NotApplicableConfigSource(),
                LocalFileConfigSource()
            ),
            parser
        )
        every { parser.parse(any()) } returns SampleServices.simpleService
        assertThat(config.load()).isEqualTo(SampleServices.simpleService)
        verify { parser.parse(any()) }
    }

}