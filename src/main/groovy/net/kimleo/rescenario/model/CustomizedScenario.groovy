package net.kimleo.rescenario.model

class CustomizedScenario extends Scenario {
    String name
    String type
    Map<String, Object> yaml
    CustomizedScenario(Map yaml) {
        super(yaml)
        this.name = yaml.name
        this.type = yaml.type
        this.yaml = yaml
    }
}
