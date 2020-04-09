package microchaos.model

import java.util.*

data class Behavior(
    val commands: List<Command>,
    val response: List<Response> = emptyList()
) {
    val rangeEnds: Array<Double> by lazy { this.initRangeEnds() }
    val totalProbability = response.sumByDouble { it.probability }

    fun drawResponse(randomizer: Random = Random()): Response {
        assert(this.response.isNotEmpty()) { "Empty response list" }
        val sample = randomizer.nextDouble()
        val selectedRange = rangeEnds.binarySearch(sample)
        return if (selectedRange >= 0) {
            this.response[minOf(selectedRange + 1, this.response.size - 1)]
        } else {
            val insertionPoint = -(selectedRange + 1)
            this.response[insertionPoint]
        }
    }

    private fun initRangeEnds(): Array<Double> {
        if (this.response.isEmpty()) {
            return emptyArray()
        }
        if (this.totalProbability == 0.0) {
            return arrayOf(0.0)
        }
        var sum = 0.0
        return this.response
            .map { it.probability / totalProbability }
            .map { sum += it; sum }
            .toTypedArray()
    }
}