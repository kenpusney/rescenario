package net.kimleo.res.definition.iad

import net.kimleo.res.definition.IAsDefinition
import net.kimleo.res.execution.scenario.handlers.ScenarioHandlerRegistry
import net.kimleo.res.model.Definition
import net.kimleo.res.model.DefinitionType

@DefinitionType("handlers")
class HandlerIAD implements IAsDefinition {

    @Override
    def tryDef(handlers, Definition definition) {
        for (def handlerInfo: handlers) {
            if (handlerInfo instanceof String) {
                ScenarioHandlerRegistry.defaultRegistry().registerClass(handlerInfo)
            }
        }
    }
}
