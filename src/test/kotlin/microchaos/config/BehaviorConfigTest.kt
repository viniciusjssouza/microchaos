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

internal class BehaviorConfigTest {

    class NotApplicableBehaviorConfigSource : BehaviorConfigSource {
        override fun canBeUsed() = false
        override fun loadConfiguration() = throw UnsupportedOperationException()
        override fun onConfigChanged(listener: ConfigChangeListener) = throw UnsupportedOperationException()
    }

    class LocalFileBehaviorConfigSource : BehaviorConfigSource {
        override fun canBeUsed() = true
        override fun loadConfiguration() = FileInputStream(ModelFiles.simpleService())
        override fun onConfigChanged(listener: ConfigChangeListener) = throw UnsupportedOperationException()
    }

    private val parser = mockk<ServiceSpecParser>()

    @Test
    fun `when no config source can be used`() {
        val config = BehaviorConfig(
            arrayOf(
                NotApplicableBehaviorConfigSource()
            ),
            parser
        )
        assertThrows(IllegalArgumentException::class.java) { config.load() }
    }

    @Test
    fun `when a config source is used`() {
        val config = BehaviorConfig(
            arrayOf(
                NotApplicableBehaviorConfigSource(),
                LocalFileBehaviorConfigSource()
            ),
            parser
        )
        every { parser.parse(any()) } returns SampleServices.simpleService
        assertThat(config.load()).isEqualTo(SampleServices.simpleService)
        verify { parser.parse(any()) }
    }

}