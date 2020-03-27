package net.kimleo.rescenario.definition.iad


import net.kimleo.rescenario.definition.IAsDefinition
import net.kimleo.rescenario.definition.DataFileLoader
import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType

import java.nio.file.Paths

@DefinitionType("data")
class DataIAD implements IAsDefinition<List<String>>{
    @Override
    def tryDef(List<String> data, Definition definition) {
        data.each { dataFile ->
            def dataPath = Paths.get(definition.parent().toString(), dataFile)
            definition.parameters << DataFileLoader.load(dataPath)
        }
    }
}
