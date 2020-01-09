package microchaos.model

open class NonRunnableModelFactory : ImplFactory() {

    override fun getServiceImpl(service: Service) = service

    override fun getEndpointImpl(endpoint: Endpoint) = endpoint

    override fun getCommandImpl(command: Command) = command

}