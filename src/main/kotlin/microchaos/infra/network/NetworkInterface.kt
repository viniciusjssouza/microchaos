package microchaos.infra.network

import microchaos.infra.Environment

data class NetworkInterface(val name: String) {

    private var defaultGatewayAddress: String

    init {
        this.defaultGatewayAddress = getDefaultGatewayIpAddress().address
    }

    fun disable() {
        Environment.runCLICommand("ifconfig $name down")
    }

    fun enable() {
        Environment.runCLICommand("ifconfig $name up")
        Environment.runCLICommand("route add default gw ${this.defaultGatewayAddress}")
    }

    fun getDefaultGatewayIpAddress(): IpAddress {
        val command = "netstat -nr | awk '{print $2}' | head -n3 | tail -n1"
        return IpAddress(Environment.runCLICommand(command).trim())
    }
}