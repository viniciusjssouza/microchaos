package microchaos.support

import java.util.*

class BiasedRandom(private val nextDouble:Double = 0.0) : Random() {

    override fun nextDouble(): Double {
        return this.nextDouble
    }
}