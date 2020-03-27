package net.kimleo.res.definition.iad

import net.kimleo.res.definition.IAsDefinition
import net.kimleo.res.model.BasicScenario
import net.kimleo.res.model.Definition
import net.kimleo.res.model.DefinitionType
import net.kimleo.res.model.meta.MetaInfo

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
