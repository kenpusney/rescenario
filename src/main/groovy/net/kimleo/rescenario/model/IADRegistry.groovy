package net.kimleo.rescenario.model

import groovy.util.logging.Log
import net.kimleo.rescenario.model.iad.HandlersIAD
import net.kimleo.rescenario.model.iad.RequirementIAD
import net.kimleo.rescenario.model.iad.ScenarioIAD
import net.kimleo.rescenario.model.iad.ServiceIAD
import net.kimleo.rescenario.model.iad.TemplateIAD

@Log
class IADRegistry {
    static IADRegistry INSTANCE

    Map<String, IAsDefinition> iads = [:]

    def loadDefault() {
        if (!iads.isEmpty()) {
            return
        }

        registerDefinition(ServiceIAD.class)
        registerDefinition(HandlersIAD.class)
        registerDefinition(TemplateIAD.class)
        registerDefinition(ScenarioIAD.class)
        registerDefinition(RequirementIAD.class)

    }

    def registerDefinition(String key, IAsDefinition iad) {
        if (iads.containsKey(key)) {
            log.warning("Redefined definition ${key}")
        }
        iads.put(key, iad)
    }

    def registerDefinition(clz) {
        DefinitionType annotation = clz.getAnnotation(DefinitionType.class)
        if (annotation == null || !clz.interfaces.contains(IAsDefinition.class)) {
            throw new IllegalStateException("${clz} is not a scenario handler")
        }
        String key = annotation.value();
        registerDefinition(key, (IAsDefinition) clz.newInstance())
    }

    static IADRegistry get() {
        if (INSTANCE == null){
            INSTANCE = new IADRegistry()
            INSTANCE.loadDefault()
        }

        return INSTANCE
    }

}
