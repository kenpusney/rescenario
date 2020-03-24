package net.kimleo.rescenario.execution.scenario.handlers

import groovy.util.logging.Log
import net.kimleo.rescenario.execution.Retriever
import net.kimleo.rescenario.execution.scenario.ScenarioHandler
import net.kimleo.rescenario.execution.scenario.ScenarioType

@ScenarioType("handler")
@Log
class DefinitionHandler implements ScenarioHandler {

    private final ScenarioHandlerRegistry registry

    DefinitionHandler(ScenarioHandlerRegistry registry) {
        this.registry = registry
    }

    @Override
    void executeScenario(Map<String, Object> map, Retriever retriever) {
        if (map.tag && map.block) {
            log.info("Defining a handler with tag '${map.tag}'")
            if (registry.retrieve(map.tag as String) != null) {
                log.warning("WARNING!! Redefinition!!")
            }
            registry.register(map.tag as String, {m, r ->
                log.info("Executing customized handler ${map.tag}")
                (Eval.me(map.block as String) as ScenarioHandler).executeScenario(m, r)
            })
        }
    }
}
