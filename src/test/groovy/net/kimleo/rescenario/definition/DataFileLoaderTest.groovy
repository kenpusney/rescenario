package net.kimleo.rescenario.definition


import spock.lang.Specification

import java.nio.file.Paths

class DataFileLoaderTest extends Specification {

    def "should load json data successfully"() {
        when:
        def parameterData = DataFileLoader.load(Paths.get("example/TestData/JsonData.json"))

        then:
        parameterData.dataSets.size() == 1
        parameterData.dataSets.first()."name" == "KimmyLeo"
    }

    def "should load yaml data successfully"() {
        when:
        def parameterData = DataFileLoader.load(Paths.get("example/TestData/YamlData.yaml"))

        then:
        parameterData.dataSets.size() == 2
        parameterData.dataSets[0]."name" == "Kimmy"
        parameterData.dataSets[1]."name" == "Leo"
    }

    def "should load Csv data successfully"() {
        when:
        def parameterData = DataFileLoader.load(Paths.get("example/TestData/CsvData.csv"))

        then:
        parameterData.dataSets.size() == 2
        parameterData.dataSets[0]."name" == "Kimmy Leo"
        parameterData.dataSets[1]."name" == "Hash Top"
    }

}
