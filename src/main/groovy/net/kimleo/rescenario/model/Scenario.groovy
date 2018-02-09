package net.kimleo.rescenario.model

class Scenario {
    String name
    String type = "rest"
    List<String> domain = []
    RequestAction action = null
    Map<String, String> headers = [:]
    String body = ""
    Map<String, Map<String, Object>> expect = [:]
    Map<String, String> store = [:]
    int delay = 0

    Scenario(Map yaml) {
        this.name = yaml.name ?: "[No name]"
        this.delay = yaml.delay ?: 0
    }

    static Scenario from(Map yaml) {
        Scenario scenario = new Scenario(yaml)

        if (yaml.type && yaml.type != 'rest') {
            return new CustomizedScenario(yaml)
        }
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
