package net.kimleo.rescenario.execution


import groovy.util.logging.Log
import net.kimleo.rescenario.execution.scenario.handlers.ScenarioHandlerRegistry
import net.kimleo.rescenario.model.BasicScenario
import net.kimleo.rescenario.model.Definition

@Log
class Executor {

    Set<Definition> execQueue = []

    class Execution {
        List<Execution> dependency = []
        List<BasicScenario> scenarios = []
    }

    Execution buildExecutionGraph(Definition definition) {
        def execution = new Execution()
        execution.scenarios = definition.scenarios

        if (!definition.dependency?.empty) {
            def dependency = definition.dependency.collect { buildExecutionGraph(it) }
            execution.dependency = dependency
        }

        return execution
    }

    List<BasicScenario> scenarioSteps(Execution execution) {
        if (execution.dependency) {
            List<BasicScenario> dependencySteps =
                    execution.dependency.collectMany { scenarioSteps(it) }
            return dependencySteps + execution.scenarios
        }
        return execution.scenarios
    }


    void executeParametricScenarios(List<BasicScenario> scenarios, Definition definition) {
        List<Map> dataSets = definition.dataSets()

        dataSets.each { data ->

            def context = new ExecutionContext()
            context.data = data
            context.definition = definition
            context.initializeData()
            scenarios.each { scenario ->
                executeScenario(context, scenario)
            }
        }

    }

    void execute(Definition definition) {
        def execution = buildExecutionGraph(definition)
        def scenarios = scenarioSteps(execution)

        executeParametricScenarios(scenarios, definition)
    }


    private void executeScenario(ExecutionContext context, BasicScenario scenario) {

        log.info("Start running scenario [$scenario.name]")

        checkDelay(scenario)
        ScenarioHandlerRegistry
                    .defaultRegistry()
                    .retrieve(scenario.type)
                    ?.executeScenario(scenario.yaml, context)
    }

    private void checkDelay(BasicScenario scenario) {
        if (scenario.delay) {
            log.info("Delay for ${scenario.delay}ms")
            sleep(scenario.delay)
        }
    }
}
