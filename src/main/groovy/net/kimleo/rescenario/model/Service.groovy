package net.kimleo.rescenario.model

import org.apache.http.client.utils.URIBuilder

class Service {
    String name
    List<String> domain
    URI uri

    static Service buildService(Map yaml) {
        Service service = new Service()

        service.name = yaml.name
        service.domain = yaml.domain
        if (yaml.url) {
            service.uri = new URI(yaml.url)
        } else {
            service.uri = new URIBuilder().setHost(yaml.host)
                    .setScheme(yaml.scheme ?: "http")
                    .setPath(yaml.endpoint)
                    .setPort(yaml.port ?: (yaml.scheme == "https" ? 443 : 80))
                    .build()
        }

        return service
    }
}
