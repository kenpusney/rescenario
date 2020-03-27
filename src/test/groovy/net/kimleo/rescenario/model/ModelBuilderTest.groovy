package net.kimleo.rescenario.model

import org.yaml.snakeyaml.Yaml
import spock.lang.Specification

class ModelBuilderTest extends Specification {

    def yaml = new Yaml()

    def "build service"() {
        when:
        def service = Service.buildService(yaml.load("""
name: Service Name
domain: [domain1, domain2]
url: "https://example.com"
"""))
        then:
        service.domain == ["domain1", "domain2"]
        service.name == "Service Name"
        service.uri == new URI("https://example.com")
    }

    def "build service from url segments"() {
        when:
        def service = Service.buildService(yaml.load("""
name: Service Name
domain: [domain1, domain2]
scheme: https
host: example.com
endpoint: hello
port: 8080
"""))
        then:
        service.domain == ["domain1", "domain2"]
        service.name == "Service Name"
        service.uri == new URI("https://example.com:8080/hello")
    }

    def "build service from url segments with default http scheme"() {
        when:
        def service = Service.buildService(yaml.load("""
name: Service Name
domain: [domain1, domain2]
host: example.com
endpoint: hello
port: 8080
"""))
        then:
        service.domain == ["domain1", "domain2"]
        service.name == "Service Name"
        service.uri == new URI("http://example.com:8080/hello")
    }

    def "build service from url segments with default http port"() {
        when:
        def service = Service.buildService(yaml.load("""
name: Service Name
domain: [domain1, domain2]
scheme: http
host: example.com
endpoint: hello
"""))
        then:
        service.domain == ["domain1", "domain2"]
        service.name == "Service Name"
        service.uri == new URI("http://example.com:80/hello")
    }

    def "build service from url segments with default https port"() {
        when:
        def service = Service.buildService(yaml.load("""
name: Service Name
domain: [domain1, domain2]
scheme: https
host: example.com
endpoint: hello
"""))
        then:
        service.domain == ["domain1", "domain2"]
        service.name == "Service Name"
        service.uri == new URI("https://example.com:443/hello")
    }


}
