package net.kimleo.rescenario.execution

import spock.lang.Specification

class ExecutionContextTest extends Specification {


    def "should fork"() {
        when:
        def context = new ExecutionContext()
        context.store = [name: "forked"]
        def forked = context.fork()
        then:
        forked.parent == context
        forked.store == context.store
    }

    def "should initialize empty context"() {
        when:
        def context = new ExecutionContext()
        then:
        context.currentDefinition == null
        context.parent == null
        context.history.empty
        context.store.isEmpty()
    }
}
