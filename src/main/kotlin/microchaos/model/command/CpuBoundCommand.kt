package microchaos.model.command

import microchaos.infra.logging.debug
import microchaos.infra.logging.loggerFor
import microchaos.infra.number.round
import microchaos.model.Distribution
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.ForkJoinPool
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

class CpuBoundCommand(duration: Distribution): TimeBoundedCommand(duration) {
    companion object {
        private val log =
            loggerFor<CpuBoundCommand>()
    }

    override fun run() {
        val ioTime = this.generateDistributionSample()
        log.info("Starting CPU Bounded execution for ${ioTime.round(2)} ms")
        val ncores = Runtime.getRuntime().availableProcessors()

        // Use a separated fork join pool to not interfere with the main one
        var forkJoinPool: ForkJoinPool? = null
        try {
            forkJoinPool = ForkJoinPool(ncores)
            forkJoinPool.submit {
                IntStream.range(0, ncores)
                    .parallel()
                    .mapToObj { this.cpuIntensiveTask(ioTime) }
                    .collect(Collectors.toList())
            }.get()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } catch (e: ExecutionException) {
            throw RuntimeException(e)
        } finally {
            forkJoinPool?.shutdown()
        }
    }

    private fun cpuIntensiveTask(ioTime: Double): Boolean {
        log.debug { "Starting for thread: ${Thread.currentThread().id}" }
        val startTime = System.currentTimeMillis()
        // perform some encryption algorithm to keep cpu busy
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        val secretKey = keyGenerator.generateKey()
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        while (System.currentTimeMillis() - startTime < ioTime) {
            val uuid = UUID.randomUUID().toString()
            val plaintTextByteArray = uuid.toByteArray(Charset.forName("UTF-8"))
            cipher.doFinal(plaintTextByteArray)
            Thread.yield()
        }
        return true
    }
}
