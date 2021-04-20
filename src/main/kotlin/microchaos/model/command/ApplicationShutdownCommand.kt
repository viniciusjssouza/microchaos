package microchaos.model.command

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import microchaos.infra.HealthCheck
import microchaos.infra.logging.loggerFor
import kotlin.system.exitProcess

class ApplicationShutdownCommand : Command() {

    companion object {
        private val log = loggerFor<ApplicationShutdownCommand>()
    }

    override fun run(): Any {
        log.error("Finishing application with some error")
        HealthCheck.isOk = false
        GlobalScope.launch {
            delay(30_000)
            exitProcess(1)
        }
        return true
    }
}