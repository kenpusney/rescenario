package net.kimleo.rescenario.definition.iad

import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType
import net.kimleo.rescenario.definition.IAsDefinition
import net.kimleo.rescenario.model.Template

@DefinitionType("templates")
class TemplateIAD implements IAsDefinition<List<Map>> {
    @Override
    def tryDef(List<Map> template, Definition definition) {
        template.each { tmpl ->
            definition.templates << Template.buildTemplate(tmpl);
        }
    }
}
