package net.kimleo.rescenario.execution.scenario.handlers

import groovy.text.GStringTemplateEngine
import groovy.util.logging.Log
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification
import net.kimleo.rescenario.execution.ExecutionContext
import net.kimleo.rescenario.execution.Retriever
import net.kimleo.rescenario.execution.scenario.ScenarioHandler
import net.kimleo.rescenario.execution.scenario.ScenarioType
import net.kimleo.rescenario.model.rest.RestScenario

@ScenarioType("rest")
@Log
class RestHandler implements ScenarioHandler {
    GStringTemplateEngine engine = new GStringTemplateEngine()

    private void execute(ExecutionContext context, RestScenario scenario) {
        log.info("Start running scenario [$scenario.name]")

        def service = context.service(*(scenario.domain)).first()

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
            binding.setVariable("context", context)
            context[var] = new GroovyShell(binding).evaluate(expr)
            println("$var == ${context[var]}")
        }
    }

    @Override
    void executeScenario(Map<String, Object> yaml, ExecutionContext context) {
        execute(context, RestScenario.from(yaml));
    }
}
