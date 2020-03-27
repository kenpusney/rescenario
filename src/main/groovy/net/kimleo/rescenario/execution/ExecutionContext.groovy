package net.kimleo.rescenario.execution


import net.kimleo.rescenario.model.Definition

class ExecutionContext implements Retriever {
    Map<String, Object> store = [:]
    Definition definition
    Map data = [:]

    Object put(String key, Object value) {
        store.put(key, value)
    }

    Object putAt(String key, Object value) {
        put(key, value)
    }

    Object getAt(String key) {
        return get(key)
    }

    Object get(String key) {
        def value = store.get(key)
        if (value == null) {
            return data.get(key)
        }
        return value
    }
}
