package net.kimleo.rescenario.definition.iad

import net.kimleo.rescenario.definition.DefinitionLoader
import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType
import net.kimleo.rescenario.definition.IAsDefinition

import java.nio.file.Paths

@DefinitionType("requires")
class RequirementIAD implements IAsDefinition<List<String>> {
    @Override
    def tryDef(List<String> requires, Definition definition) {
        requires.each { String req ->
            def reqPath = Paths.get(definition.parent().toString(), req)
            definition.dependency << DefinitionLoader.fromPath(reqPath)
        }
    }
}
