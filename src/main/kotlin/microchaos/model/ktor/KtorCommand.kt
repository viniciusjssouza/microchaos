package microchaos.model.ktor

//abstract class KtorCommand(
//    type: String,
//    duration: Distribution?,
//    httpRequest: HttpRequest?
//) : Command(type, duration, httpRequest) {
//
//    companion object {
//        private val commandFactory = mapOf(
//            "ioBounded" to { command: Command -> IoBoundedCommand(command) },
//            "cpuBounded" to { command: Command -> KtorCpuBoundedCommand(command) },
//            "request" to { command: Command -> RequestCommand(command) },
//            "networkFailure" to { command: Command -> LinuxNetworkFailureCommand(command) },
//            "applicationShutdown" to { command: Command -> ApplicationShutdownCommand(command) }
//        )
//
//        fun from(command: Command): KtorCommand {
//            val commandFactoryFn = this.commandFactory.getOrElse(command.type, {
//                throw IllegalArgumentException("No ktor command found for  ${command.type}")
//            })
//            return commandFactoryFn(command)
//        }
//    }
//
//    constructor(command: Command) : this(command.type, command.duration, command.httpRequest)
//}

