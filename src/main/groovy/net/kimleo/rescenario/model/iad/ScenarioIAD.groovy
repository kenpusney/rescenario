package net.kimleo.rescenario.model.iad

import net.kimleo.rescenario.model.BasicScenario
import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType
import net.kimleo.rescenario.model.IAsDefinition
import net.kimleo.rescenario.model.meta.MetaInfo

@DefinitionType("scenario")
class ScenarioIAD implements IAsDefinition<List<Map>> {
    @Override
    def tryDef(List<Map> scenarios, Definition definition) {

        scenarios.each { scene ->
            if ("meta" in scene) {
                definition.meta << MetaInfo.of(scene)
            } else {
                definition.scenarios << new BasicScenario(scene);
            }
        }
    }
}
