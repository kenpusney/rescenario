package net.kimleo.res.execution.scenario

import net.kimleo.res.execution.ExecutionContext

@FunctionalInterface
interface ScenarioHandler {
    void executeScenario(Map<String, Object> yaml, ExecutionContext context)

    default void shortCut(Map<String, Object> yaml, ExecutionContext context) {
        executeScenario(yaml, context)
    }
}