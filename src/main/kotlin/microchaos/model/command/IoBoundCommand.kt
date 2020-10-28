package microchaos.model.command

import microchaos.infra.logging.loggerFor
import microchaos.infra.number.round
import microchaos.model.Distribution

class IoBoundCommand(duration: Distribution): TimeBoundedCommand(duration) {
    companion object {
        private val log =
            loggerFor<IoBoundCommand>()
    }

    override fun run() {
        val ioTime = this.generateDistributionSample()
        log.info("Starting IO Bounded command for ${ioTime.round(2)} ms")
        Thread.sleep(ioTime.toLong())
    }
}
