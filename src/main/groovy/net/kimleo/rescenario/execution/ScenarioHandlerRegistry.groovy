package net.kimleo.rescenario.execution

import groovy.util.logging.Log

@Log
class ScenarioHandlerRegistry {
    Map<String, ScenarioHandler> handlers = [:];

    void register(String name, ScenarioHandler handler) {
        handlers[name] = handler
    }

    ScenarioHandler retrieve(String name) {
        return handlers[name]
    }

    static ScenarioHandlerRegistry registry = null

    static ScenarioHandlerRegistry defaultRegistry() {
        if (registry != null) {
            return registry
        }
        registry = new ScenarioHandlerRegistry()

        registry.register("exec", { Map<String, String> map, Retriever retriever ->
            if (map.command) {
                List<String> args = map.args ?: []
                args.add(0, map.command)
                log.info("Executing command: ${map.command}")
                def proc = args.execute()
                proc.waitForProcessOutput(System.out, System.err)
                if (map.store) {
                    retriever.put(map.store, proc.exitValue())
                }

            }
        })

        registry.register("show", { Map<String, String> map, Retriever retriever ->
            if (map.variable) {
                if (map.variable instanceof List) {
                    for (v in map.variable)
                        log.info("Value for variable '${v}' is [${retriever.get(v)}]")
                } else {
                    log.info("Value for variable '${map.variable}' is [${retriever.get(map.variable)}]")
                }
            }
        })

        registry.register("eval", { Map<String, String> map, Retriever retriever ->
            if (map.expr && map.store) {
                log.info("Store '${map.store}' with expression: ${map.expr}")
                retriever.put(map.store, Eval.me("it", retriever, map.expr))
            }
        })

        registry.register("handler", {Map<String, String> map, Retriever retriever ->
            if (map.tag && map.block) {
                log.info("Defining a handler with tag '${map.tag}'")
                if (registry.retrieve(map.tag) != null) {
                    log.warning("WARNING!! Redefinition!!")
                }
                registry.register(map.tag, {m, r ->
                    log.info("Executing customized handler ${map.tag}")
                    ((ScenarioHandler) Eval.me(map.block)).executeScenario(m, r)
                })
            }
        })


        return registry
    }
}
