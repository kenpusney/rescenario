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

import static org.hamcrest.CoreMatchers.containsString

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

            def services = ret.service(*(scenario.domain))

            services.each { service ->
                checkDelay(scenario)

                log.info("Calling service [$service.name] with $scenario.action")

                RequestSpecification requestSpec
                ResponseSpecification responseSpec

                requestSpec = RestAssured
                        .given()
                        .baseUri(service.uri.toString())
                        .headers(scenario.headers.collectEntries { String key, value ->
                    [(key): engine.createTemplate(value).make(context.store).toString()]
                }).body(scenario.body ?: "")

                if (scenario.expect.headers)
                    log.info("Matching headers: ${scenario.expect.headers}")
                if (scenario.expect.status)
                    log.info("Matching status: ${scenario.expect.status}")
                responseSpec = requestSpec.log().all().expect()
                        .headers(scenario.expect.headers ?: [:])
                        .statusCode(scenario.expect.status ?: 200).log().all()

                scenario.expect.body.each { key, path ->
                    log.info("Matching body [$key] with variable path: [$path]")
                    if (path.startsWith("\$")) {
                        responseSpec.body(key, containsString(Eval.me("it", ret, path.trim().substring(1))))
                    } else {
                        responseSpec.body(key, containsString(ret[path]))
                    }
                }

                def response = requestSpec
                        .request(scenario.action.method,
                        engine.createTemplate(scenario.action.path)
                                .make(context.store).toString())

                scenario.store.each { var, path ->
                    if (path.trim().startsWith("\$")) {
                        log.info("Set variable \$$var with expression: [$path]}")
                        ret[var] = Eval.me("response", response, path.trim().substring(1))
                        println("$var == ${ret[var]}")
                    } else {
                        log.info("Set variable \$$var with path: [$path]: ${response.body.path(path)} ")
                        ret[var] = response.body.path(path)
                    }
                }
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
