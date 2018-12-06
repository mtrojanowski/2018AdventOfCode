import day1Puzzle1.FrequencyCalculator
import spock.lang.Specification

import java.nio.file.Paths

class Day1KtTest extends Specification {

    def "test frequency calculator"() {
        given:
        def file = new File(Paths.get(this.getClass().getResource("testInput").toURI()).toString())
        def calc = new FrequencyCalculator()

        when:
        def result = calc.calculate(file)

        then:
        result == 8
    }

    def "another test of the calculator"() {
        given:
        def file = new File(Paths.get(this.getClass().getResource("testInput").toURI()).toString())
        def calc = new FrequencyCalculator()

        when:
        def result = calc.calibrate(file)

        then:
        result == 11
    }
}
