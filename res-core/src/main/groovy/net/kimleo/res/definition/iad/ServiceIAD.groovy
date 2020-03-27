package net.kimleo.res.definition.iad

import net.kimleo.res.definition.IAsDefinition
import net.kimleo.res.model.Definition
import net.kimleo.res.model.DefinitionType
import net.kimleo.res.model.Service

@DefinitionType("services")
class ServiceIAD implements IAsDefinition<List<Map>> {

    @Override
    def tryDef(List<Map> services, Definition definition) {
        services.each { service ->
            definition.services << Service.buildService(service)
        }
    }
}
