import spock.lang.Specification

class SafeAreaFinderTest extends Specification {
    def "FindLargestSeparateArea"() {
        given:
            def coordinatesRaw = ["1, 1",
                                  "1, 6",
                                  "8, 3",
                                  "3, 4",
                                  "5, 5",
                                  "8, 9"]
            def finder = new SafeAreaFinder(coordinatesRaw)

        when:
            def area = finder.findLargestSeparateArea()
        then:
            area == 17
    }

    def "find area close to locations"() {
        given:
        def coordinatesRaw = ["1, 1",
                              "1, 6",
                              "8, 3",
                              "3, 4",
                              "5, 5",
                              "8, 9"]
        def finder = new SafeAreaFinder(coordinatesRaw)

        when:
        def area = finder.findAreaWithinXToLocations(32)
        then:
        area == 16
    }
}
