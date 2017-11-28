package net.kimleo.rescenario.model

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
    List<Scenario> scenarios = []
    List<Service> services = []

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

        if ("scenario" in defn.content) {
            defn.content.scenario.each { scene ->
                if ("meta" in scene) {
                    defn.meta << MetaInfo.of(scene)
                } else {
                    defn.scenarios << Scenario.from(scene);
                }
            }
        }

        if ("service" in defn.content) {
            defn.content.service.each { service ->
                defn.services << Service.buildService(service)
            }
        }

        return (defn)
    }
}