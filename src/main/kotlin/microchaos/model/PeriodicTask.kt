package microchaos.model

import microchaos.infra.logging.loggerFor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

data class PeriodicTask(
    val period: Long,
    val behavior: Behavior
) {
    private val executorService = Executors.newSingleThreadScheduledExecutor()

    companion object {
        val log = loggerFor<PeriodicTask>()
    }

    fun schedule() {
        executorService.scheduleAtFixedRate(this::runCommands, period, period, TimeUnit.MILLISECONDS)
        log.info("Periodic tasks scheduled with period $period")
    }

    fun stop() {
        executorService.shutdown()
    }

    private fun runCommands() {
        log.info("Running periodic task commands...")
        behavior.commands.forEach { command -> command.run() }
    }

}
