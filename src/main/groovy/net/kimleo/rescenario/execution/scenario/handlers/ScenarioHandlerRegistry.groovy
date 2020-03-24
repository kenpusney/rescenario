package net.kimleo.rescenario.execution.scenario.handlers


import groovy.util.logging.Log
import net.kimleo.rescenario.execution.scenario.ScenarioHandler
import net.kimleo.rescenario.execution.scenario.ScenarioType

@Log
class ScenarioHandlerRegistry {
    Map<String, ScenarioHandler> handlers = [:];

    void register(String name, ScenarioHandler handler) {
        handlers[name] = handler
    }

    void register(Class clz, Object... params) {
        ScenarioType annotation = clz.getAnnotation(ScenarioType.class)
        if (annotation == null || !clz.interfaces.contains(ScenarioHandler.class)) {
            throw new IllegalStateException("${clz} is not a scenario handler")
        }
        String value = annotation.value();
        handlers[value] = (ScenarioHandler) clz.newInstance(params)
    }

    void registerClass(String className, Object... params) {
        register(Class.forName(className), params);
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
        registry.register(ExecHandler.class)
        registry.register(ShowHandler.class)
        registry.register(EvalHandler.class)
        registry.register(DefinitionHandler.class, registry)
        registry.register(RestHandler.class)
        return registry
    }
}
