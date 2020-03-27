package net.kimleo.res.definition

import net.kimleo.res.model.Definition
import org.yaml.snakeyaml.Yaml

import java.nio.file.Files
import java.nio.file.Path

class DefinitionLoader {

    static Map<Path, Definition> cache = [:]

    static Definition fromPath(Path path, reload = false) {
        def iadRegistry = IADRegistry.get()

        if (path in cache && !reload) {
            return cache[path]
        }
        def definition = new Definition()
        definition.path = path
        InputStream input = Files.newInputStream(path)

        definition.content = new Yaml().load(input)
        cache[path] = definition

        for (def iad: iadRegistry.iads) {
            if (iad.key in definition.content) {
                iad.value.tryDef(definition.content[iad.key], definition)
            }
        }

        return (definition)
    }
}
