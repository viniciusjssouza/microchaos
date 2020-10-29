package microchaos.model.command

import microchaos.infra.logging.loggerFor
import java.util.*
import kotlin.math.roundToInt

class MemoryAllocationCommand(private val amount: Int) : Command() {
    companion object {
        const val ESTIMATED_OBJECT_SIZE = 20.5
        const val KB_IN_BYTES = 1000
    }

    private val totalAllocations = ((amount * KB_IN_BYTES) / ESTIMATED_OBJECT_SIZE).roundToInt()
    val storage = ArrayList<Any>()


    override fun run() {
        repeat(totalAllocations) {
            storage.add(Object())
        }
    }

}