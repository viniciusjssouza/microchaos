package microchaos.model.ktor

import microchaos.infra.logging.loggerFor
import microchaos.infra.number.round
import microchaos.model.Command

internal class IoBoundedCommand(command: Command) : KtorCommand(command) {
    companion object {
        private val log =
            loggerFor<IoBoundedCommand>()
    }

    override fun run() {
        val ioTime = this.generateDistributionSample()
        log.info("Starting IO Bounded command for ${ioTime.round(2)} ms")
        Thread.sleep(ioTime.toLong())
    }
}