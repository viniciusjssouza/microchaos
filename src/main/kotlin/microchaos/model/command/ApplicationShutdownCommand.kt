package microchaos.model.command

import microchaos.infra.logging.loggerFor
import kotlin.system.exitProcess

class ApplicationShutdownCommand : Command() {

    companion object {
        private val log = loggerFor<ApplicationShutdownCommand>()
    }

    override fun run(): Any {
        log.error("Finishing application with some error")
        exitProcess(1)
    }
}