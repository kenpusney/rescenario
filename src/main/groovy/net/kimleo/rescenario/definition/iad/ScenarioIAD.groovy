package net.kimleo.rescenario.definition.iad

import net.kimleo.rescenario.model.BasicScenario
import net.kimleo.rescenario.model.Definition
import net.kimleo.rescenario.model.DefinitionType
import net.kimleo.rescenario.definition.IAsDefinition
import net.kimleo.rescenario.model.meta.MetaInfo

@DefinitionType("scenarios")
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
