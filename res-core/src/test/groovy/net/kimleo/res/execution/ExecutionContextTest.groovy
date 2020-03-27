package net.kimleo.res.execution

import spock.lang.Specification

class ExecutionContextTest extends Specification {

    def "should initialize empty context"() {
        when:
        def context = new ExecutionContext()
        then:
        context.definition == null
        context.store.isEmpty()
    }
}
