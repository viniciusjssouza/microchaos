package microchaos.infra.network

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NetworkInterfaceTest {

    private val networkInterface = NetworkInterface("eth0")

    @Test
    fun `get default gateway address`() {
        val gatewayAddress = networkInterface.getDefaultGatewayIpAddress()
        println("Gateway address found: $gatewayAddress")
        assertThat(gatewayAddress.isValid()).isTrue()
    }
}