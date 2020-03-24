package net.kimleo.rescenario.execution

interface Retriever {
    Object get(String key)
    Object put(String key, Object value)
    Object getAt(String key)
    Object putAt(String key, Object value)
}