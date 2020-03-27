package net.kimleo.res.definition.iad

import net.kimleo.res.definition.DataFileLoader
import net.kimleo.res.definition.IAsDefinition
import net.kimleo.res.model.Definition
import net.kimleo.res.model.DefinitionType

import java.nio.file.Paths

@DefinitionType("data")
class DataIAD implements IAsDefinition<List<String>>{
    @Override
    def tryDef(List<String> data, Definition definition) {
        data.each { dataFile ->
            def dataPath = Paths.get(definition.parent().toString(), dataFile)
            definition.data << DataFileLoader.load(dataPath)
        }
    }
}
