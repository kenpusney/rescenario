package net.kimleo.res.execution.scenario.handlers

import groovy.util.logging.Log
import net.kimleo.res.execution.ExecutionContext
import net.kimleo.res.execution.scenario.ScenarioHandler
import net.kimleo.res.execution.scenario.ScenarioType

@ScenarioType("exec")
@Log
class ExecHandler implements ScenarioHandler{
    @Override
    void executeScenario(Map<String, Object> yaml, ExecutionContext context) {
        if (yaml.command) {
            List<String> args = yaml.args ?: []
            args.add(0, yaml.command as String)
            execute(args, context, yaml)
        }
    }


    def execute(List<String> args, ExecutionContext context, Map<String, Object> yaml) {
        def proc = args.execute()
        log.info("Executing command: ${args}")
        proc.waitForProcessOutput(System.out, System.err)
        if (yaml.store) {
            context.put(yaml.store as String, proc.exitValue())
        }
    }

    @Override
    void shortCut(Map<String, Object> yaml, ExecutionContext context) {
        if (yaml.exec instanceof List) {
            execute((List<String>) yaml.exec, context, yaml)
        }
    }
}
