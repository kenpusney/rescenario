package net.kimleo.rescenario.model.rest

import groovy.text.GStringTemplateEngine
import groovy.text.SimpleTemplateEngine
import io.restassured.specification.ResponseSpecification

import static org.hamcrest.CoreMatchers.containsString
import static org.hamcrest.Matchers.allOf
import static org.hamcrest.Matchers.hasToString

class Expections {
    Map<String, Object> expect = [:]

    Map<String, String> headers = [:]

    int status = 200

    def body = [:]

    Expections(Map<String, Object> expect) {
        this.expect = expect
        this.status = expect.status ?: 200 as int
        this.headers = (expect.headers ?: [:]) as Map<String, String>
        this.body = expect.body ?: [:]
    }

    def match(ResponseSpecification response, Map context) {
        def engine = new GStringTemplateEngine();
        response.headers(headers.collectEntries {key, value ->
            [(key): (engine.createTemplate(value).make(context).toString())]
        }).statusCode(status)

        if (body instanceof Map<String, Object>) {
            body.each { String key, value ->
                response.body(key, hasToString(
                        containsString(
                                engine.createTemplate(Objects.toString(value))
                                        .make(context).toString())))
            }
        } else if (body != null) {
            response.body(containsString(Objects.toString(body)))
        }
    }
}
