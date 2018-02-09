package net.kimleo.rescenario.execution

@FunctionalInterface
interface ScenarioHandler {
    void executeScenario(Map<String, Object> yaml, Retriever retriever)
}