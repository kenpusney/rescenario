package net.kimleo.rescenario.execution

import io.restassured.RestAssured
import io.restassured.http.Method
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification
import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.Scenario
import net.kimleo.rescenario.model.Service
import net.kimleo.rescenario.model.meta.MetaInfo
import org.hamcrest.Matcher

import static org.hamcrest.CoreMatchers.containsString

class Executor {
    void exec(Definition definition, ExecutionContext context = null) {
        boolean initial = (context==null)
        context = context ?: new ExecutionContext()


        context.currentDefinition = definition

        exec(context, initial)
    }

    void exec(ExecutionContext context, boolean initial) {
        List<Scenario> scenarios = context.currentDefinition.scenarios
        List<MetaInfo> meta = context.currentDefinition.meta

        def ret = new Retriever(context)

        scenarios.each { scenario ->
            def services = ret.service(*(scenario.domain))

            services.each { service ->
                def requestSpec = RestAssured
                        .given()
                        .baseUri(service.uri.toString())
                        .pathParams(context.store)
                        .headers(scenario.headers)
                        .body(scenario.body)

                def responseSpec = requestSpec.expect()
                        .headers(scenario.expect.headers ?: [:])
                        .statusCode(scenario.expect.statusCode ?: 200)

                scenario.expect.body.each { key, string ->
                    responseSpec.body(key, containsString(string))
                }

                def response = requestSpec
                        .request(scenario.action.method, scenario.action.path)


                scenario.store.each { var, path ->
                    ret[var] = response.body().path(path)
                }
            }
        }
    }


}
