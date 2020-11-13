package microchaos.model.command

import microchaos.infra.logging.loggerFor
import java.util.*
import kotlin.math.roundToInt

class MemoryAllocationCommand(val amount: Int) : Command() {
    companion object {
        private val log = loggerFor<MemoryAllocationCommand>()
        const val ELEMENT_SIZE = 1.0
        const val KB_IN_BYTES = 1024
    }

    val storage = ArrayList<Any>()

    override fun run() {
        log.info("Allocating $amount KB")
        storage.add(ByteArray((amount * KB_IN_BYTES / ELEMENT_SIZE).roundToInt()))
    }
}