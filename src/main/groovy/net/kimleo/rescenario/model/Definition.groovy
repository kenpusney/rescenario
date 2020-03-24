package net.kimleo.rescenario.model

import net.kimleo.rescenario.execution.scenario.handlers.ScenarioHandlerRegistry
import net.kimleo.rescenario.model.meta.MetaInfo
import org.yaml.snakeyaml.Yaml

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Definition {
    Path path
    Object content
    List<Definition> dependency = []
    List<MetaInfo> meta = []
    List<BasicScenario> scenarios = []
    List<Service> services = []
    List<Template> templates = []

    static Map<Path, Definition> cache = [:]

    static Definition fromPath(Path path, reload = false) {
        if (path in cache && !reload) {
            return cache[path]
        }
        def defn = new Definition()
        defn.path = path
        InputStream input = Files.newInputStream(path)

        defn.content = new Yaml().load(input)
        cache[path] = defn

        if ("requires" in defn.content) {
            defn.content.requires.each { req ->
                def reqPath = Paths.get(path.getParent().toString(), req)
                defn.dependency << fromPath(reqPath)
            }
        }

        if ("handlers" in defn.content) {
            def registry = ScenarioHandlerRegistry.defaultRegistry()
            defn.content.handlers.each { handler ->
                registry.registerClass(handler)
            }
        }

        if ("scenario" in defn.content) {
            defn.content.scenario.each { scene ->
                if ("meta" in scene) {
                    defn.meta << MetaInfo.of(scene)
                } else {
                    defn.scenarios << new BasicScenario(scene);
                }
            }
        }

        if ("service" in defn.content) {
            defn.content.service.each { service ->
                defn.services << Service.buildService(service)
            }
        }

        if ("template" in defn.content) {
            defn.content.template.each { template ->
                defn.templates << Template.buildTemplate(template);
            }
        }

        return (defn)
    }
}