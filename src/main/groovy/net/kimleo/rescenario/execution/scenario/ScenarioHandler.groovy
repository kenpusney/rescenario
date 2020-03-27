package net.kimleo.rescenario.execution.scenario

import net.kimleo.rescenario.execution.ExecutionContext

@FunctionalInterface
interface ScenarioHandler {
    void executeScenario(Map<String, Object> yaml, ExecutionContext context)

    default void shortCut(Map<String, Object> yaml, ExecutionContext context) {
        executeScenario(yaml, context)
    }
}