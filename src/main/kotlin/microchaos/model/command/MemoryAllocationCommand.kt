package microchaos.model.command

import java.util.*
import kotlin.math.roundToInt

// jackson requires that the constructor input be a public property :(
// Issue: https://github.com/FasterXML/jackson-module-kotlin/issues/131
class MemoryAllocationCommand(val amount: Int) : Command() {
    companion object {
        const val ESTIMATED_OBJECT_SIZE = 20.5
        const val KB_IN_BYTES = 1000
    }

    private val totalAllocations: Int
    val storage = ArrayList<Any>()

    init {
        totalAllocations = ((amount * KB_IN_BYTES) / ESTIMATED_OBJECT_SIZE).roundToInt()
    }


    override fun run() {
        repeat(totalAllocations) {
            storage.add(Object())
        }
    }

}