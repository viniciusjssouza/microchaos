package microchaos.config

import io.mockk.every
import io.mockk.mockkObject
import microchaos.infra.Configuration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ConsulBehaviorConfigSourceTest {

    private val configSource = ConsulBehaviorConfigSource()

    @BeforeEach
    fun setupConfiguration() {
        mockkObject(Configuration);
    }

    @Test
    fun `canBeUsed when consul url is empty`() {
        every { Configuration.consulUrl } returns null

        assertThat(configSource.canBeUsed()).isFalse()
    }

    @Test
    fun `canBeUsed when service name is null`() {
        every { Configuration.serviceName } returns null

        assertThat(configSource.canBeUsed()).isFalse()
    }

    @Test
    fun `canBeUsed when all parameters are provided`() {
        every { Configuration.serviceName } returns "test"
        every { Configuration.consulUrl } returns "127.0.0.1:1234"

        assertThat(configSource.canBeUsed()).isTrue()
    }
}