package net.kimleo.rescenario

import net.kimleo.rescenario.execution.Executor
import net.kimleo.rescenario.model.Definition

import java.nio.file.Path
import java.nio.file.Paths

class App {
    static void main(String[] args) {
        Path path = Paths.get(args[0])
        def definition = Definition.fromPath(path)

        def executor = new Executor()
        executor.exec(definition)
    }
}
