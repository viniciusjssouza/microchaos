package microchaos.model.command

import microchaos.model.Distribution

abstract class TimeBoundedCommand(val duration: Distribution): Command() {

    protected fun generateDistributionSample(): Double {
        return this.duration.sample()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeBoundedCommand

        if (duration != other.duration) return false

        return true
    }

    override fun hashCode(): Int {
        return duration.hashCode()
    }

    override fun toString(): String {
        return "TimeBoundedCommand(duration=$duration)"
    }
}
