package net.kimleo.rescenario.execution

import net.kimleo.rescenario.model.Definition

class ExecutionContext {
    Map<String, Object> store = [:]
    ExecutionContext parent = null
    Definition currentDefinition

    void setCurrentDefinition(Definition definition) {
        if (currentDefinition == null) history.push(currentDefinition)
        currentDefinition = definition
    }

    Stack<Definition> history = []

    ExecutionContext fork() {
        def forked = new ExecutionContext(parent: this)
        forked.store.putAll(store)
        return forked
    }
}
