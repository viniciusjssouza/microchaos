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
import java.io.InputStream
import kotlin.concurrent.thread

internal class BehaviorConfigTest {

    class NotApplicableBehaviorConfigSource : BehaviorConfigSource {
        override fun canBeUsed() = false
        override fun loadConfiguration() = throw UnsupportedOperationException()
        override fun onConfigChanged(listener: ConfigChangeListener) = throw UnsupportedOperationException()
    }

    class LocalFileBehaviorConfigSource : BehaviorConfigSource {
        override fun canBeUsed() = true
        override fun loadConfiguration() = FileInputStream(ModelFiles.simpleService())
        override fun onConfigChanged(listener: ConfigChangeListener) {
            thread {
                Thread.sleep(50)
                listener(InputStream.nullInputStream())
            }
        }
    }

    private val parser = mockk<ServiceSpecParser>()

    @Test
    fun `load config when no config source can be used`() {
        val config = BehaviorConfig(
            arrayOf(
                NotApplicableBehaviorConfigSource()
            ),
            parser
        )
        assertThrows(IllegalArgumentException::class.java) { config.load() }
    }

    @Test
    fun `load config when a config source is used`() {
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

    @Test
    fun `on model change`() {
        var changed = false
        val config = BehaviorConfig(
            arrayOf(
                NotApplicableBehaviorConfigSource(),
                LocalFileBehaviorConfigSource()
            ),
            parser
        )
        every { parser.parse(any()) } returns SampleServices.simpleService
        config.onModelChange { model ->
            changed = true
            assertThat(model).isEqualTo(SampleServices.simpleService)
        }

        Thread.sleep(100)
        verify { parser.parse(any()) }
        assertThat(changed).isTrue()
    }
}