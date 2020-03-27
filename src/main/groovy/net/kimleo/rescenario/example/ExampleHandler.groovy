package net.kimleo.rescenario.example

import net.kimleo.rescenario.execution.ExecutionContext
import net.kimleo.rescenario.execution.scenario.ScenarioHandler
import net.kimleo.rescenario.execution.scenario.ScenarioType

@ScenarioType("example")
class ExampleHandler implements ScenarioHandler {
    @Override
    void executeScenario(Map<String, Object> yaml, ExecutionContext context) {
        println "Hello, it runs example handler!"
    }
}
