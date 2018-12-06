import spock.lang.Specification

class PolymerShrinkerTest extends Specification {
    def "should shrink polymer"() {
        given:
            def polymer = "dabAcCaCBAcCcaDA"
            def shrinker = new PolymerShrinker(polymer)

        when:
            def shrunkPolymer = shrinker.shrink()

        then:
            shrunkPolymer == "dabCBAcaDA"
    }

    def "check removeRange at end of string"() {
        given:
        def polymer = "agsjdhsdkshdkaA"
        def shrinker = new PolymerShrinker(polymer)

        when:
        def shrunkPolymer = shrinker.shrink()

        then:
        shrunkPolymer == "agsjdhsdkshdk"
    }

    def "should shrink polymer even better"() {
        given:
            def polymer = "dabAcCaCBAcCcaDA"
            def shrinker = new PolymerShrinker(polymer)

        when:
            def evenMoreShrunkPolymer = shrinker.shrinkEvenBetter()

        then:
            evenMoreShrunkPolymer == "daDA"
    }
}