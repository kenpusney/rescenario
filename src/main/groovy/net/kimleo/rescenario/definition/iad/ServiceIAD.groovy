package net.kimleo.rescenario.definition.iad

import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType
import net.kimleo.rescenario.definition.IAsDefinition
import net.kimleo.rescenario.model.Service

@DefinitionType("services")
class ServiceIAD implements IAsDefinition<List<Map>> {

    @Override
    def tryDef(List<Map> services, Definition definition) {
        services.each { service ->
            definition.services << Service.buildService(service)
        }
    }
}
