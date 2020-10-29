package microchaos.infra

import java.lang.management.ManagementFactory

fun measureExecutionTime(funcToMeasure: () -> Any): Long {
    val start = System.currentTimeMillis()
    funcToMeasure()
    return System.currentTimeMillis() - start
}

fun measureMemoryAllocationInKB(funcToMeasure: () -> Any): Double {
    val memoryBefore = getUsedMemory()
    funcToMeasure()
    val memoryAfter = getUsedMemory()
    return (memoryAfter - memoryBefore) / 1000.0;
}

fun getUsedMemory(): Long {
    waitGC()
    return with(Runtime.getRuntime()) {
        totalMemory() - freeMemory()
    }
}

private fun waitGC() {
    val currGcCount: Long = getGcCount()
    System.gc();
    while (getGcCount() == currGcCount) {
    }
}

private fun getGcCount(): Long {
    return ManagementFactory.getGarbageCollectorMXBeans().map { garbageCollectorMXBean ->
        val count = garbageCollectorMXBean.collectionCount
        if (count < 0) 0L else count
    }.sum()
}