package net.kimleo.rescenario.model.iad

import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType
import net.kimleo.rescenario.model.IAsDefinition
import net.kimleo.rescenario.model.Template

@DefinitionType("template")
class TemplateIAD implements IAsDefinition<List<Map>> {
    @Override
    def tryDef(List<Map> template, Definition definition) {
        template.each { tmpl ->
            definition.templates << Template.buildTemplate(tmpl);
        }
    }
}
