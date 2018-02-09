package net.kimleo.rescenario.model

class Template {
    String name
    String template
    static Template buildTemplate(Map yaml) {
        def template = new Template()
        template.name = yaml.name
        template.template = yaml.template
    }
}
