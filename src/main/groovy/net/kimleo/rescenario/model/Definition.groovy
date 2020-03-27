package net.kimleo.rescenario.model

import groovy.util.logging.Log
import net.kimleo.rescenario.definition.IADRegistry
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
}