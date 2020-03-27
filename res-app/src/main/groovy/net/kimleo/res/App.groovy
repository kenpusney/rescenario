package net.kimleo.res

import net.kimleo.res.definition.DefinitionLoader
import net.kimleo.res.execution.Executor

import java.nio.file.Path
import java.nio.file.Paths

class App {
    static void main(String[] args) {
        Path path = Paths.get(args[0])
        def definition = DefinitionLoader.fromPath(path)

        def executor = new Executor()
        executor.execute(definition)
    }
}
