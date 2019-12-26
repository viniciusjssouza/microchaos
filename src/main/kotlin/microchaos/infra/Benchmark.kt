package microchaos.infra

fun measureExecutionTime(funcToMeasure: () -> Any): Long {
    val start = System.currentTimeMillis()
    funcToMeasure()
    return System.currentTimeMillis() - start
}