package microchaos.service.spec.model


data class Service(
    val name: String,
    val type: String,
    val port: Int,
    val endpoints: List<Endpoint> = arrayListOf<Endpoint>(),
    val periodicTasks: List<PeriodicTask> = arrayListOf<PeriodicTask>()
) {
}
