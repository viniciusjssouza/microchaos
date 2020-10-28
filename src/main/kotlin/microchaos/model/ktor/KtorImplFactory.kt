package microchaos.model.ktor

import microchaos.model.Endpoint
import microchaos.model.ImplFactory
import microchaos.model.Service

class KtorImplFactory : ImplFactory() {

    override fun getServiceImpl(service: Service) = KtorService(service)
    override fun getEndpointImpl(endpoint: Endpoint) = KtorEndpoint.from(endpoint)
}