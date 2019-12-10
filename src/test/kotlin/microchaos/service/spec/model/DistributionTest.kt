package microchaos.service.spec.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

internal class DistributionTest {

    @Test
    fun `unsupported distribution type`() {
        val distribution = Distribution(type="unknown", mean = 100.0, stdDeviation = 2.0)
        assertThatIllegalArgumentException().isThrownBy { distribution.sample() }
    }

    @Test
    fun `run "normal" distribution `() {
        val distribution = Distribution(type="normal", mean = 100.0, stdDeviation = 2.0)
        assertThat(distribution.sample()).isBetween(90.0, 110.0)
    }

    @Test
    fun `run "logNormal" distribution `() {
        val distribution = Distribution(type="logNormal", mean = 6.0, stdDeviation = 0.25)
        assertThat(distribution.sample()).isBetween(0.0, 10000.0)
    }
}