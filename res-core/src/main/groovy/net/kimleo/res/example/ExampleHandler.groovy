package net.kimleo.res.example

import net.kimleo.res.execution.ExecutionContext
import net.kimleo.res.execution.scenario.ScenarioHandler
import net.kimleo.res.execution.scenario.ScenarioType

@ScenarioType("example")
class ExampleHandler implements ScenarioHandler {
    @Override
    void executeScenario(Map<String, Object> yaml, ExecutionContext context) {
        println "Hello, it runs example handler!"
    }
}
