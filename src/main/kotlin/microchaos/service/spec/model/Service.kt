package microchaos.service.spec.model


data class Service(
    val name: String,
    val type: String,
    val port: Int,
    val endpoints: ArrayList<Endpoint> = arrayListOf<Endpoint>()
) {
}
