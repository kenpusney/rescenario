package net.kimleo.rescenario.execution.scenario

import net.kimleo.rescenario.execution.Retriever

@FunctionalInterface
interface ScenarioHandler {
    void executeScenario(Map<String, Object> yaml, Retriever retriever)
}