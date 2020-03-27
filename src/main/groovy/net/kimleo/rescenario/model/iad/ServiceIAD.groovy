package net.kimleo.rescenario.model.iad

import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType
import net.kimleo.rescenario.model.IAsDefinition
import net.kimleo.rescenario.model.Service

@DefinitionType("service")
class ServiceIAD implements IAsDefinition<List<Map>> {

    def tryDef(List<Map> services, Definition definition) {
        services.each { service ->
            definition.services << Service.buildService(service)
        }
    }
}
