package microchaos.model

import microchaos.infra.measureMemoryAllocationInKB
import microchaos.model.command.MemoryAllocationCommand
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage
import org.junit.Test
import org.junit.jupiter.api.parallel.Isolated
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@Isolated // other tests might interfere with the memory allocation measurements
internal class MemoryAllocationCommandTest {

    @ParameterizedTest
    @ValueSource(ints = [1000, 10_000, 100_000])
    fun `run a memory allocation command`(amount: Int) {
        val command = MemoryAllocationCommand(amount)
        val allocatedMemory = measureMemoryAllocationInKB {
            command.run()
        }
        assertThat(allocatedMemory).isCloseTo(amount.toDouble(), Percentage.withPercentage(10.0))
    }

    @Test
    fun `repeated memory allocation command`() {
        val command = MemoryAllocationCommand(1000)
        val allocatedMemory = measureMemoryAllocationInKB {
            command.run()
            command.run()
            command.run()
        }
        assertThat(allocatedMemory).isCloseTo(3_000.0, Percentage.withPercentage(10.0))
    }
}