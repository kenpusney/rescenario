package net.kimleo.rescenario.definition

import com.opencsv.CSVReaderHeaderAware
import groovy.json.JsonSlurper
import net.kimleo.rescenario.model.ParameterData
import org.yaml.snakeyaml.Yaml

import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Function

class DataFileLoader {

    static Map<String, Function<Path, ParameterData>> extensionHandlers = [
            ".json": DataFileLoader.&loadJson,
            ".csv": DataFileLoader.&loadCSV,
            ".yaml": DataFileLoader.&loadYaml,
    ]

    static ParameterData load(Path path) {
        def filename = path.toString()
        for (def handler: extensionHandlers) {
            if (filename.endsWith(handler.key)) {
                return handler.value(path)
            }
        }

        return new ParameterData();
    }

    static ParameterData loadJson(Path path) {
        def slurper = new JsonSlurper()

        def result = slurper.parse(path.toFile())

        if (result instanceof List) {
            return new ParameterData(result)
        }
        return new ParameterData([result])
    }

    static ParameterData loadCSV(Path path) {
        def csvReader = new CSVReaderHeaderAware(Files.newBufferedReader(path))

        def records = new LinkedList<Map>()

        def record = csvReader.readMap()
        while (record != null) {
            records.add(record)
            record = csvReader.readMap()
        }

        return new ParameterData(records)
    }

    static ParameterData loadYaml(Path path) {
        def yaml = new Yaml()

        def data = yaml.load(Files.newInputStream(path))

        if (data instanceof List) {
            return new ParameterData(data)
        }
        return new ParameterData([data])
    }
}
