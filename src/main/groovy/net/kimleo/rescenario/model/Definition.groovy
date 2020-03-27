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

    static Map<String, IAsDefinition> definitionsRegistry = [:]

    static Definition fromPath(Path path, reload = false) {
        loadRegistry()
        if (path in cache && !reload) {
            return cache[path]
        }
        def defn = new Definition()
        defn.path = path
        InputStream input = Files.newInputStream(path)

        defn.content = new Yaml().load(input)
        cache[path] = defn

        for (def iad: definitionsRegistry) {
            if (iad.key in defn.content) {
                iad.value.tryDef(defn.content[iad.key], defn)
            }
        }

        return (defn)
    }

    static def registerDefinition(String key, IAsDefinition iad) {
        if (definitionsRegistry.containsKey(key)) {
            log.warning("Redefined definition ${key}")
        }
        definitionsRegistry.put(key, iad)
    }

    static def registerDefinition(clz) {
        DefinitionType annotation = clz.getAnnotation(DefinitionType.class)
        if (annotation == null || !clz.interfaces.contains(IAsDefinition.class)) {
            throw new IllegalStateException("${clz} is not a scenario handler")
        }
        String value = annotation.value();
        definitionsRegistry.put(value, (IAsDefinition) clz.newInstance())
    }

    static def loadRegistry() {
        if (!definitionsRegistry.isEmpty()) {
            return
        }

        registerDefinition(ServiceIAD.class)
        registerDefinition(HandlersIAD.class)
        registerDefinition(TemplateIAD.class)
        registerDefinition(ScenarioIAD.class)
        registerDefinition(RequirementIAD.class)

    }
}