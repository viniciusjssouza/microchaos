package microchaos.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PeriodicTaskTest() {

    class CountCommand : Command(type = "count") {
        var count = 1

        override fun run(): Any {
            return count++
        }
    }

    @Test
    fun `run all commands periodically`() {
        val commands = Array(2) { CountCommand() }.toList()
        val periodicTask = PeriodicTask(period = 1000, behavior = Behavior(commands))

        periodicTask.schedule()

        assertThat(commands.map { it.count }).isEqualTo(listOf(1, 1))
        Thread.sleep(1000);
        assertThat(commands.map { it.count }).isEqualTo(listOf(2, 2))
        Thread.sleep(1000);
        assertThat(commands.map { it.count }).isEqualTo(listOf(3, 3))
        periodicTask.stop()
    }
}