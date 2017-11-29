package net.kimleo.rescenario.execution

import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.Service

import java.util.concurrent.LinkedBlockingDeque

class Retriever {
    private final ExecutionContext context

    private Multimap<String, Service> services = LinkedListMultimap.create()

    Retriever(ExecutionContext context) {
        this.context = context

        initializeData()
    }

    def initializeData() {
        def visited = new HashSet<Definition>()
        Queue<Definition> queue = new LinkedBlockingDeque<Definition>([context.currentDefinition])

        while(!queue.isEmpty()) {
            def definition = queue.poll()
            if (!(definition in visited)) {
                definition.dependency.each {
                    if (!(it in visited)) queue.add(it)
                }
            }

            definition.services.each { service ->
                service.domain.each { domain ->
                    services.put(domain, service)
                }
            }
            visited << definition
        }

    }

    Object put(String key, Object value) {
        context.store.put(key, value)
    }

    Object putAt(String key, Object value) {
        put(key, value)
    }

    Object getAt(String key) {
        return get(key)
    }

    Object get(String key) {
        ExecutionContext current = context
        while(current != null) {
            if (key in current.store) {
                return current.store[key]
            }
            current = current.parent
        }
        return null
    }

    def service(String... domains) {
        def services = services.get(domains.first())
        List<String> remains = domains.drop(1)
        while(!remains.isEmpty() && services.isEmpty()) {
            services = services.collect { remains.first() in it.domain }
            remains = remains.drop(1)
        }
        return services
    }
}
