package net.kimleo.res.definition.iad

import net.kimleo.res.definition.DefinitionLoader
import net.kimleo.res.definition.IAsDefinition
import net.kimleo.res.model.Definition
import net.kimleo.res.model.DefinitionType

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
