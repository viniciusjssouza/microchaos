package microchaos.parser.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class ModelMapperDeserializer(
    modelClass: Class<*>,
    private val originalDeserializer: JsonDeserializer<*>,
    private val mapperFn: (source: Any) -> Any): ResolvableDeserializer, StdDeserializer<Any>(modelClass) {

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Any {
        val source = originalDeserializer.deserialize(p, ctxt)
        return mapperFn(source)
    }

    override fun resolve(ctxt: DeserializationContext?) {
        (originalDeserializer as ResolvableDeserializer).resolve(ctxt)
    }
}