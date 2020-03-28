package microchaos.infra

object Configuration {

    // Local file behavior configuration
    val localFileConfigPath = envVar("CONFIG_PATH")

    // Consul behavior configuration
    val serviceName = envVar("SERVICE_NAME")
    val consulUrl = envVar("CONSUL_URL")


    private fun envVar(name: String): String? {
        return System.getenv().getOrDefault(name, null);
    }
}