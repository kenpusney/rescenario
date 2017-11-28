package net.kimleo.rescenario.model

class Scenario {
    String name
    List<String> domain
    RequestAction action
    Map<String, String> headers
    String body
    Map<String, Map<String, Object>> expect
    Map<String, String> store

    static Scenario from(Map yaml) {
        Scenario scenario = new Scenario()

        scenario.name = yaml.name ?: "[No name]"
        scenario.domain = wrap(yaml.domain ?: "default")
        scenario.action = RequestAction.of(yaml.action, yaml.method, yaml.path)
        scenario.headers = yaml.headers ?: [:]
        scenario.body = yaml.body
        scenario.expect = yaml.expect ?: [:]
        scenario.store = yaml.store ?: [:]
        return scenario
    }

    static List<String> wrap(o) {
        if (o instanceof List) {
            return o
        }
        return [o] as List<String>
    }
}
