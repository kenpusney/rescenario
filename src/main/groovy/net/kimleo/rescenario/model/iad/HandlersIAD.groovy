package net.kimleo.rescenario.model.iad

import net.kimleo.rescenario.execution.scenario.handlers.ScenarioHandlerRegistry
import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType
import net.kimleo.rescenario.model.IAsDefinition

@DefinitionType("handlers")
class HandlersIAD implements IAsDefinition {

    def tryDef(handlers, Definition definition) {
        for (def handlerInfo: handlers) {
            if (handlerInfo instanceof String) {
                ScenarioHandlerRegistry.defaultRegistry().registerClass(handlerInfo)
            }
        }
    }
}
