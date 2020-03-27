package net.kimleo.res.example


import spock.lang.Specification

class ExampleHandlerTest extends Specification {

    def handler = new ExampleHandler()

    def "should run example handler"(){
        expect:
        handler.executeScenario([:], null)
    }
}
