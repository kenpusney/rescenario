package net.kimleo.rescenario.execution.scenario.handlers

import groovy.util.logging.Log
import net.kimleo.rescenario.execution.Retriever
import net.kimleo.rescenario.execution.scenario.ScenarioHandler
import net.kimleo.rescenario.execution.scenario.ScenarioType

@ScenarioType("eval")
@Log
class EvalHandler implements ScenarioHandler {
    @Override
    void executeScenario(Map<String, Object> map, Retriever retriever) {
        if (map.expr && map.store) {
            log.info("Store '${map.store}' with expression: ${map.expr}")
            retriever.put(map.store as String,
                    Eval.me("it", retriever, map.expr as String))
        }
    }
}
