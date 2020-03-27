package net.kimleo.res.execution.scenario.handlers

import groovy.util.logging.Log
import net.kimleo.res.execution.ExecutionContext
import net.kimleo.res.execution.scenario.ScenarioHandler
import net.kimleo.res.execution.scenario.ScenarioType

@ScenarioType("script")
@Log
class ScriptHandler implements ScenarioHandler {

    private final ScenarioHandlerRegistry registry

    ScriptHandler(ScenarioHandlerRegistry registry) {
        this.registry = registry
    }

    @Override
    void executeScenario(Map<String, Object> map, ExecutionContext context) {
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

    @Override
    void shortCut(Map<String, Object> yaml, ExecutionContext context) {
        if (yaml.script) {
            def map = new HashMap<>(yaml)
            map.block = yaml.script
            executeScenario(map, context)
        }
    }
}
