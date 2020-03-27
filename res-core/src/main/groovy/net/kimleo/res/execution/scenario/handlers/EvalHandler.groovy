package net.kimleo.res.execution.scenario.handlers

import groovy.util.logging.Log
import net.kimleo.res.execution.ExecutionContext
import net.kimleo.res.execution.scenario.ScenarioHandler
import net.kimleo.res.execution.scenario.ScenarioType

@ScenarioType("eval")
@Log
class EvalHandler implements ScenarioHandler {
    @Override
    void executeScenario(Map<String, Object> map, ExecutionContext context) {
        if (map.expr && map.store) {
            log.info("Store '${map.store}' with expression: ${map.expr}")
            context.put(map.store as String,
                    Eval.me("it", context, map.expr as String))
        }
    }

    @Override
    void shortCut(Map<String, Object> yaml, ExecutionContext context) {
        if (yaml.eval) {
            def map = new HashMap<>(yaml)
            map.expr = yaml.eval
            executeScenario(map, context)
        }
    }
}
