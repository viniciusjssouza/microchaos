package microchaos.parser

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import microchaos.model.ImplFactory
import microchaos.model.NonRunnableModelFactory
import microchaos.model.ServiceModel
import microchaos.parser.jackson.ModelMapperDeserializer
import java.lang.IllegalArgumentException


class YamlBehaviorSpecParser(private val implFactory: ImplFactory = NonRunnableModelFactory()) : ServiceSpecParser {

    private val mapper: ObjectMapper = ObjectMapper(YAMLFactory())

    init {
        this.mapper
            .registerModule(customDeserializerModule())
            .findAndRegisterModules()
    }

    private fun customDeserializerModule(): SimpleModule {
        val module = SimpleModule()
       module.setDeserializerModifier(DeserializerWrapper(implFactory))
        return module
    }

    override fun parse(representation: String): ServiceModel {
        return this.mapper.readValue(representation, ServiceModel::class.java)
    }
}

class DeserializerWrapper(private val implFactory: ImplFactory) : BeanDeserializerModifier() {

    override fun modifyDeserializer(
        config: DeserializationConfig?,  beanDesc: BeanDescription, deserializer: JsonDeserializer<*>?
    ): JsonDeserializer<*>? {
        val modifiedFromParent = deserializer ?:
            throw IllegalArgumentException("No deserializer found for type ${beanDesc.beanClass.name}")
        return if (implFactory.hasRegisteredMapper(beanDesc.beanClass)) {
            val mapperFn = implFactory.getMapper(beanDesc.beanClass)
            ModelMapperDeserializer(beanDesc.beanClass, modifiedFromParent, mapperFn)
        } else {
            modifiedFromParent
        }
    }
}