import spock.lang.Specification

class FabricCalculatorTest extends Specification {
    def "should CalculateOverlappingSquares"() {
        given:
            def data = ["#1 @ 1,3: 4x4",
                         "#2 @ 3,1: 4x4",
                         "#3 @ 5,5: 2x2"]

            def calculator = new FabricCalculator(data)

        when:
            def result = calculator.calculateOverlappingSquares()

        then:
            result == 4L
    }

    def "should find not overlapping claim"() {
        given:
        def data = ["#1 @ 1,3: 4x4",
                    "#2 @ 3,1: 4x4",
                    "#3 @ 5,5: 2x2"]

        def calculator = new FabricCalculator(data)

        when:
        def result = calculator.findNotOverlappingClaim()

        then:
        result == "3"
    }
}
