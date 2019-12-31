package microchaos.model.ktor

import microchaos.infra.Environment
import microchaos.infra.logging.loggerFor
import microchaos.infra.network.NetworkInterface
import microchaos.infra.number.round
import microchaos.model.Command

internal class LinuxNetworkFailureCommand(
    command: Command,
    private val environment: Environment = Environment,
    private val networkInterface: NetworkInterface = NetworkInterface("eth0")
) : KtorCommand(command) {

    companion object {
        private val log = loggerFor<LinuxNetworkFailureCommand>()
    }

    override fun run(): Any {
        if (!this.environment.isLinuxOS()) {
            throw UnsupportedOperationException("Network Failure command is available only on Unix environments")
        }

        val ioTime = this.generateDistributionSample()
        var interfaceUp = true
        try {
            this.networkInterface.disable()
            interfaceUp = false
            log.info("Network interface disabled for ${ioTime.round(2)} ms")
            Thread.sleep(ioTime.toLong())
        } finally {
            if (!interfaceUp) {
                this.networkInterface.enable()
                log.info("Network interface re-enabled")
            }
        }
        return true
    }
}