package net.kimleo.res.definition.iad

import net.kimleo.res.definition.IAsDefinition
import net.kimleo.res.model.Definition
import net.kimleo.res.model.DefinitionType
import net.kimleo.res.model.Template

@DefinitionType("templates")
class TemplateIAD implements IAsDefinition<List<Map>> {
    @Override
    def tryDef(List<Map> template, Definition definition) {
        template.each { tmpl ->
            definition.templates << Template.buildTemplate(tmpl);
        }
    }
}
