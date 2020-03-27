package net.kimleo.rescenario.definition.iad

import net.kimleo.rescenario.execution.scenario.handlers.ScenarioHandlerRegistry
import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType
import net.kimleo.rescenario.definition.IAsDefinition

@DefinitionType("handlers")
class HandlersIAD implements IAsDefinition {

    @Override
    def tryDef(handlers, Definition definition) {
        for (def handlerInfo: handlers) {
            if (handlerInfo instanceof String) {
                ScenarioHandlerRegistry.defaultRegistry().registerClass(handlerInfo)
            }
        }
    }
}
