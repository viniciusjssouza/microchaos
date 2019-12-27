package microchaos.service.spec.model

data class PeriodicTask(
    val period: Long,
    val behavior: Behavior
) {
}
