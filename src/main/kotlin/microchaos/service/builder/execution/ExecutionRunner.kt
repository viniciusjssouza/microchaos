package microchaos.service.builder.execution

import microchaos.infra.logging.debug
import microchaos.infra.logging.loggerFor
import microchaos.infra.number.round
import microchaos.service.spec.model.Execution
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.ForkJoinPool
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator


abstract class ExecutionRunner(val spec: Execution) {

    abstract fun run(): Any

    protected fun generateDistributionSample(): Double {
        return spec.distribution?.sample() ?:
            throw IllegalArgumentException("Every ${this::class.simpleName} must contain a distribution")
    }
}

class IoBoundedRunner(spec: Execution) : ExecutionRunner(spec) {
    companion object {
        private val log = loggerFor<IoBoundedRunner>()
    }

    override fun run() {
        val ioTime = this.generateDistributionSample()
        log.info("starting IO Bounded execution for ${ioTime.round(2)} ms")
        Thread.sleep(ioTime.toLong())
    }
}

class CpuBoundedRunner(spec: Execution) : ExecutionRunner(spec) {
    companion object {
        private val log = loggerFor<CpuBoundedRunner>()
    }

    override fun run() {
        val ioTime = this.generateDistributionSample()
        log.info("starting CPU Bounded execution for ${ioTime.round(2)} ms")
        val ncores = Runtime.getRuntime().availableProcessors()

        // Use a separated fork join pool to not interfere with the main one
        var forkJoinPool: ForkJoinPool? = null
        try {
            forkJoinPool = ForkJoinPool(ncores)
            forkJoinPool.submit {
                IntStream
                    .range(0, ncores)
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

class RequestRunner(spec: Execution) : ExecutionRunner(spec) {

    override fun run(): String {
        val client = HttpClient.newBuilder().build()
        val request = spec.httpRequest?.let {
            HttpRequest
                .newBuilder()
                .method(it.method.toUpperCase(), HttpRequest.BodyPublishers.noBody())
                .uri((URI.create(it.target)))
                .build()

        } ?: throw IllegalArgumentException("Every request execution must have a http request declaration")
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() >= 500) {
            throw RuntimeException(
                "Unexpected response from server. Status: ${response.statusCode()} Content: ${response.body()}"
            )
        }
        return response.body()
    }
}