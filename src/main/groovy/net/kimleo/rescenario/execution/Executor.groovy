package net.kimleo.rescenario.execution


import groovy.util.logging.Log
import net.kimleo.rescenario.execution.scenario.handlers.ScenarioHandlerRegistry
import net.kimleo.rescenario.model.BasicScenario
import net.kimleo.rescenario.model.Definition

@Log
class Executor {

    Set<Definition> execQueue = []

    ExecutionContext exec(Definition definition, ExecutionContext context = null) {
        boolean initial = (context == null)
        context = context ?: new ExecutionContext()

        if (definition in execQueue) {
            throw new IllegalStateException("Unexpected error: possible recursive dependency")
        }
        execQueue.add(definition)

        context.currentDefinition = definition

        return exec(context, initial)
    }

    ExecutionContext exec(ExecutionContext context, boolean initial) {
        def currentDefn = context.currentDefinition
        def scenarios = currentDefn.scenarios
        def meta = currentDefn.meta

        currentDefn.dependency.each { defn ->
            context = exec(defn, context).fork()
            context.currentDefinition = currentDefn
        }

        scenarios.each { scenario ->
            executeScenario(context, scenario)
        }

        return context
    }

    private void executeScenario(ExecutionContext context, BasicScenario scenario) {
        def ret = new RuntimeRetriever(context)

        log.info("Start running scenario [$scenario.name]")

        checkDelay(scenario)
        ScenarioHandlerRegistry
                    .defaultRegistry()
                    .retrieve(scenario.type)
                    ?.executeScenario(((BasicScenario) scenario).yaml, ret)
    }

    private void checkDelay(BasicScenario scenario) {
        if (scenario.delay) {
            log.info("Delay for ${scenario.delay}ms")
            sleep(scenario.delay)
        }
    }
}
