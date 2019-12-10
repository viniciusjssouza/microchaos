package microchaos.service.spec.model

import org.apache.commons.math3.distribution.LogNormalDistribution
import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.commons.math3.distribution.RealDistribution
import java.lang.IllegalArgumentException

data class Distribution(
    val type: String,
    val mean: Double,
    val stdDeviation: Double
) {
    private val distributionFactories = mapOf<String, () -> RealDistribution>(
        "normal" to { NormalDistribution(mean, stdDeviation) },
        "logNormal" to { LogNormalDistribution(mean, stdDeviation) }
    )


    fun sample(): Double {
        val realDistribution = distributionFactories.getOrElse(type, {
            throw IllegalArgumentException("Unsuppported distribution type: $type")
        })()
        return realDistribution.sample()
    }


}
