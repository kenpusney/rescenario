package net.kimleo.rescenario.execution.scenario.handlers

import groovy.util.logging.Log
import net.kimleo.rescenario.execution.ExecutionContext
import net.kimleo.rescenario.execution.scenario.ScenarioHandler
import net.kimleo.rescenario.execution.scenario.ScenarioType

@ScenarioType("show")
@Log
class ShowHandler implements ScenarioHandler {
    @Override
    void executeScenario(Map<String, Object> map, ExecutionContext context) {
        if (map.variable) {
            if (map.variable instanceof List) {
                for (v in map.variable)
                    log.info("Value for variable '${v}' is [${context.get(v)}]")
            } else {
                log.info("Value for variable '${map.variable}' is [${context.get(map.variable)}]")
            }
        }
    }
}
