package microchaos.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.contrib.java.lang.system.EnvironmentVariables
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class LocalFileConfigSourceTest {

    private val configSource = LocalFileConfigSource()
    private val yamlConfigFilePath = this::class.java.getResource("/simpleService.yaml").path

    @Rule @JvmField
    val envVars = EnvironmentVariables()

    @Test
    fun `should not be used when the env var is not set`() {
        assertThat(configSource.canBeUsed()).isFalse()
    }

    @Test
    fun `should be usable when the env var is set`() {
        envVars.set(LocalFileConfigSource.FILE_PATH_ENV_VAR, yamlConfigFilePath)
        assertThat(configSource.canBeUsed()).isTrue()
    }

    @Test
    fun `load config when the provided file does not exist`() {
        envVars.set(LocalFileConfigSource.FILE_PATH_ENV_VAR, "./some_inexistent_file")
        Assertions.assertThrows(ServiceConfigNotFoundException::class.java) { configSource.loadConfiguration() }
    }

    @Test
    fun `load config with success`() {
        envVars.set(LocalFileConfigSource.FILE_PATH_ENV_VAR, yamlConfigFilePath)
        assertThat(configSource.loadConfiguration()).isNotNull()
    }

}