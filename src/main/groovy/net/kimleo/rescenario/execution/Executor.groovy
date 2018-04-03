package net.kimleo.rescenario.execution

import groovy.text.GStringTemplateEngine
import groovy.util.logging.Log
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification
import net.kimleo.rescenario.model.CustomizedScenario
import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.Scenario
import net.kimleo.rescenario.model.meta.MetaInfo

@Log
class Executor {

    Set<Definition> execQueue = []

    GStringTemplateEngine engine = new GStringTemplateEngine()

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
        List<Scenario> scenarios = currentDefn.scenarios
        List<MetaInfo> meta = currentDefn.meta

        currentDefn.dependency.each { defn ->
            context = exec(defn, context).fork()
            context.currentDefinition = currentDefn
        }

        scenarios.each { scenario ->
            def ret = new Retriever(context)

            log.info("Start running scenario [$scenario.name]")

            if (scenario.type != "rest") {
                checkDelay(scenario)
                ScenarioHandlerRegistry
                        .defaultRegistry()
                        .retrieve(scenario.type)
                        ?.executeScenario(((CustomizedScenario) scenario).yaml, ret)
                return
            }

            def service = ret.service(*(scenario.domain)).first()

            checkDelay(scenario)

            log.info("Calling service [$service.name] with $scenario.action")

            RequestSpecification requestSpec = RestAssured
                    .given()
                    .baseUri(service.uri.toString())
                    .headers(scenario.headers.collectEntries { String key, value ->
                return [(key): engine.createTemplate(value).make(context.store).toString()]
            }).body(scenario.body ?: "")

            ResponseSpecification responseSpec = requestSpec.log().all().expect().log().all()

            scenario.expect.match(responseSpec, context.store)

            def response = requestSpec
                    .request(scenario.action.method,
                    engine.createTemplate(scenario.action.path)
                            .make(context.store).toString())

            scenario.store.each { var, expr ->
                log.info("Set variable \$$var with expression: $expr}")
                def binding = new Binding()
                binding.setVariable("response", response)
                if (response.contentType.contains("json")) {
                    binding.setVariable("\$json", response.body.as(Map.class))
                }
                binding.setVariable("context", ret)
                ret[var] = new GroovyShell(binding).evaluate(expr)
                println("$var == ${ret[var]}")
            }
        }

        return context
    }

    private void checkDelay(Scenario scenario) {
        if (scenario.delay) {
            log.info("Delay for ${scenario.delay}ms")
            sleep(scenario.delay)
        }
    }


}
