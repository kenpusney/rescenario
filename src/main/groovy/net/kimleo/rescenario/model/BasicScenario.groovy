package net.kimleo.rescenario.model

class BasicScenario {
    String name
    String type
    int delay = 0
    Map<String, Object> yaml

    BasicScenario(Map yaml) {
        this.name = yaml.name
        this.type = yaml.type ?: 'rest'
        this.yaml = yaml
        this.delay = yaml.delay ? yaml.delay as int : 0
    }
}
