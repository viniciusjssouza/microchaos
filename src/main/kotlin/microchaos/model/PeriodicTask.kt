package microchaos.model

data class PeriodicTask(
    val period: Long,
    val behavior: Behavior
) {
}
