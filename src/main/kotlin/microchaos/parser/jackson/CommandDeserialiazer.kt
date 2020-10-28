package microchaos.parser.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.TextNode
import microchaos.model.command.Command
import microchaos.model.command.mapping

class CommandDeserialiazer(private val defaultDeserializer: JsonDeserializer<*>) : JsonDeserializer<Command>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): Command {
        val mapper: ObjectMapper = parser.codec as ObjectMapper
        val commandNode: JsonNode = mapper.readTree(parser)
        if (commandNode is TextNode) {
            return handleEmptyCommand(commandNode)
        }
        return handleCommandWithProperties(commandNode, mapper)
    }

    private fun handleCommandWithProperties(
        commandNode: JsonNode,
        mapper: ObjectMapper
    ): Command {
        val commandProperties = commandNode.fields().next()
        val commandType = commandProperties.key
        val commandClass = this.commandToClassName(commandType)
        return mapper.treeToValue(commandProperties.value, commandClass)
    }

    private fun handleEmptyCommand(commandNode: JsonNode): Command {
        return commandToClassName(commandNode.textValue()).declaredConstructors[0].newInstance() as Command
    }

    private fun commandToClassName(commandType: String): Class<out Command> {
        return mapping.getOrElse(
            commandType,
            { throw IllegalArgumentException("Command not registered for '${commandType}'") })
    }
}