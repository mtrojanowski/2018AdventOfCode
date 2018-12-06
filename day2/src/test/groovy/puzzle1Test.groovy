import spock.lang.Specification

import java.nio.file.Paths

class puzzle1Test extends Specification {
    def "CalculateCheckSum"() {
        given:
            def file = new File(Paths.get(this.class.getResource("testInput").toURI()).toString())
            def puzzle1 = new Puzzle1()

        when:
            def checksum = puzzle1.calculateCheckSum(file)

        then:
            checksum == 12
    }

    def "find closest box"() {
        given:
            def file = new File(Paths.get(this.class.getResource("testInput").toURI()).toString())
            def puzzle1 = new Puzzle1()
        when:
            def result = puzzle1.getCommonLettersOfClosestBoxes(file)
        then:
            result == "abcde"
    }
}
