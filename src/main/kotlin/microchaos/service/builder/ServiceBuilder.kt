package microchaos.service.builder

import microchaos.service.spec.model.Service

abstract class ServiceBuilder(protected val service: Service) {

    abstract fun start(wait: Boolean = true): Any
    abstract fun stop(): Any
    abstract fun getPort(): Int
}