package net.kimleo.res.execution.scenario.handlers

import groovy.util.logging.Log
import net.kimleo.res.execution.ExecutionContext
import net.kimleo.res.execution.scenario.ScenarioHandler
import net.kimleo.res.execution.scenario.ScenarioType

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

    @Override
    void shortCut(Map<String, Object> yaml, ExecutionContext context) {
        if (yaml.show) {
            def map = new HashMap<>(yaml)
            map.variable = yaml.show
            executeScenario(map, context)
        }
    }
}
