package microchaos.model.command

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import microchaos.infra.Environment
import microchaos.infra.logging.loggerFor
import microchaos.infra.network.NetworkInterface
import microchaos.infra.number.round
import microchaos.model.Distribution

class NetworkFailureCommand(
    duration: Distribution,
    private val environment: Environment = Environment,
    private val networkInterface: NetworkInterface = NetworkInterface("eth0")
) : TimeBoundedCommand(duration) {

    companion object {
        private val log = loggerFor<NetworkFailureCommand>()
    }

    override fun run(): Any {
        if (!this@NetworkFailureCommand.environment.isLinuxOS()) {
            throw UnsupportedOperationException("Network Failure command is available only on Unix environments")
        }
        // run the command in background
        GlobalScope.launch {
            val ioTime = this@NetworkFailureCommand.generateDistributionSample()
            var interfaceUp = true
            try {
                this@NetworkFailureCommand.networkInterface.disable()
                interfaceUp = false
                log.info("Network interface disabled for ${ioTime.round(2)} ms")
                Thread.sleep(ioTime.toLong())
            } finally {
                if (!interfaceUp) {
                    this@NetworkFailureCommand.networkInterface.enable()
                    log.info("Network interface re-enabled")
                }
            }
        }
        return true
    }
}