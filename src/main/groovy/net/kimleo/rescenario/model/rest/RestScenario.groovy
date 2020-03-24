package net.kimleo.rescenario.model.rest

class RestScenario {
    String name
    List<String> domain = []
    RequestAction action = null
    Map<String, String> headers = [:]
    String body = ""
    Expections expect = new Expections([:])
    Map<String, String> store = [:]
    int delay = 0

    RestScenario(Map yaml) {
        this.name = yaml.name ?: "[No name]"
        this.delay = yaml.delay ?: 0
    }

    static RestScenario from(Map yaml) {
        RestScenario scenario = new RestScenario(yaml)

        if (yaml.type && yaml.type != 'rest') {
            throw new IllegalStateException("Not a rest scenario");
        }
        scenario.domain = wrap(yaml.domain ?: "default")
        scenario.action = RequestAction.of(yaml.action, yaml.method, yaml.path)
        scenario.headers = yaml.headers ?: [:]
        scenario.body = yaml.body
        scenario.expect = new Expections(yaml.expect ?: [:])
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
