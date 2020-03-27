package net.kimleo.rescenario.model

import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import groovy.util.logging.Log
import net.kimleo.rescenario.model.meta.MetaInfo

import java.nio.file.Path
import java.util.concurrent.LinkedBlockingDeque

@Log
class Definition {
    Path path
    Object content
    List<Definition> dependency = []
    List<MetaInfo> meta = []
    List<BasicScenario> scenarios = []
    List<Service> services = []
    List<Template> templates = []
    List<ParameterData> data = []

    private Multimap<String, Service> serviceDomainMapping = LinkedListMultimap.create()

    List<Map> dataSets() {
        List<Map> maps = data.collectMany { it.dataSets }

        if (maps.isEmpty()) {
            return [[:]]
        }

        return maps
    }

    Path parent() {
        return this.path.getParent()
    }

    def service(String... domains) {
        if (serviceDomainMapping.isEmpty()) {
            mapServiceByDomain()
        }
        def services = serviceDomainMapping.get(domains.first())
        List<String> remains = domains.drop(1)
        while(!remains.isEmpty() && serviceDomainMapping.isEmpty()) {
            services = serviceDomainMapping.collect { remains.first() in it.domain }
            remains = remains.drop(1)
        }
        return services
    }

    private def mapServiceByDomain() {
        def visited = new HashSet<Definition>()
        Queue<Definition> queue = new LinkedBlockingDeque<Definition>([this])

        while(!queue.isEmpty()) {
            def definition = queue.poll()
            if (!(definition in visited)) {
                definition.dependency.each {
                    if (!(it in visited)) queue.add(it)
                }
            }

            definition.services.each { service ->
                service.domain.each { domain ->
                    this.serviceDomainMapping.put(domain, service)
                }
            }
            visited << definition
        }
    }

}