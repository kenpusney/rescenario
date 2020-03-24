package net.kimleo.rescenario.execution.scenario.handlers

import groovy.util.logging.Log
import net.kimleo.rescenario.execution.Retriever
import net.kimleo.rescenario.execution.scenario.ScenarioHandler
import net.kimleo.rescenario.execution.scenario.ScenarioType

@ScenarioType("exec")
@Log
class ExecHandler implements ScenarioHandler{
    @Override
    void executeScenario(Map<String, Object> yaml, Retriever retriever) {
        if (yaml.command) {
            List<String> args = yaml.args ?: []
            args.add(0, yaml.command as String)
            log.info("Executing command: ${yaml.command}")
            def proc = args.execute()
            proc.waitForProcessOutput(System.out, System.err)
            if (yaml.store) {
                retriever.put(yaml.store as String, proc.exitValue())
            }
        }
    }
}
