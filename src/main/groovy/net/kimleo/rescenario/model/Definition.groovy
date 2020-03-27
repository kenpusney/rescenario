package net.kimleo.rescenario.model

import groovy.util.logging.Log
import net.kimleo.rescenario.model.meta.MetaInfo

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
    List<ParameterData> data = []

    List<Map> dataSets() {
        List<Map> maps = data.collectMany { it.dataSets }

        if (maps.isEmpty()) {
            return [[:]]
        }

        return maps
    }

    Path parent() {
        return this.path.getParent()
    }
}