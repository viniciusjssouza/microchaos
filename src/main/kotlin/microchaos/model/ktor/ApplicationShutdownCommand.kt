package microchaos.model.ktor

import microchaos.infra.logging.loggerFor
import microchaos.model.Command
import kotlin.system.exitProcess

internal class ApplicationShutdownCommand(
    command: Command
) : KtorCommand(command) {

    companion object {
        private val log = loggerFor<ApplicationShutdownCommand>()
    }

    override fun run(): Any {
        log.error("Finishing application with some error")
        return System.exit(1)
    }
}