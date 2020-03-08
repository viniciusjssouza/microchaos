package microchaos.config

import io.mockk.every
import io.mockk.mockkObject
import microchaos.infra.Configuration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LocalFileBehaviorConfigSourceTest {

    private val configSource = LocalFileBehaviorConfigSource()
    private val yamlConfigFilePath = this::class.java.getResource("/simpleService.yaml").path

    @BeforeEach
    fun setupConfiguration() {
        mockkObject(Configuration);
    }

    @Test
    fun `should not be used when the file path is not set`() {
        every { Configuration.localFileConfigPath } returns null

        assertThat(configSource.canBeUsed()).isFalse()
    }

    @Test
    fun `should be usable when the env var is set`() {
        every { Configuration.localFileConfigPath } returns yamlConfigFilePath

        assertThat(configSource.canBeUsed()).isTrue()
    }

    @Test
    fun `load config when the provided file does not exist`() {
        every { Configuration.localFileConfigPath } returns "./some_inexistent_file"

        Assertions.assertThrows(ServiceConfigNotFoundException::class.java) { configSource.loadConfiguration() }
    }

    @Test
    fun `load config with success`() {
        every { Configuration.localFileConfigPath } returns yamlConfigFilePath
        assertThat(configSource.loadConfiguration()).isNotNull()
    }

}