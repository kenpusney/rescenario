package net.kimleo.rescenario.model

import groovy.util.logging.Log
import net.kimleo.rescenario.model.iad.*
import net.kimleo.rescenario.model.meta.MetaInfo
import org.yaml.snakeyaml.Yaml

import java.nio.file.Files
import java.nio.file.Path

@Log
class Definition {
    Path path
    Object content
    List<Definition> dependency = []
    List<MetaInfo> meta = []
    List<BasicScenario> scenarios = []
    List<Service> services = []
    List<Template> templates = []

    Path parent() {
        return this.path.getParent()
    }

    static Map<Path, Definition> cache = [:]

    static Definition fromPath(Path path, reload = false) {
        def iadRegistry = IADRegistry.get()

        if (path in cache && !reload) {
            return cache[path]
        }
        def defn = new Definition()
        defn.path = path
        InputStream input = Files.newInputStream(path)

        defn.content = new Yaml().load(input)
        cache[path] = defn

        for (def iad: iadRegistry.iads) {
            if (iad.key in defn.content) {
                iad.value.tryDef(defn.content[iad.key], defn)
            }
        }

        return (defn)
    }
}