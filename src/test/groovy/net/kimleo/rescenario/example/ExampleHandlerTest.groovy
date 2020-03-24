package net.kimleo.rescenario.example

import net.kimleo.rescenario.execution.ExecutionContext
import net.kimleo.rescenario.execution.RuntimeRetriever
import spock.lang.Specification

class ExampleHandlerTest extends Specification {

    def handler = new ExampleHandler()

    def "should run example handler"(){
        expect:
        handler.executeScenario([:], null)
    }
}
