import java.util.*
import java.util.stream.Collectors
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val coordinatesRaw = Loader.getFile("day6Input").readLines()
    val finder = SafeAreaFinder(coordinatesRaw)

//    println("Find those areas!")
//    val result = finder.findLargestSeparateArea()
//    println("And the safe area is $result units large")

    println("Find the safe area!")
    val result = finder.findAreaWithinXToLocations(10_000)
    println("The safe area is $result units large")
}

class SafeAreaFinder(private val coordinatesRaw: List<String>) {
    private val boundaries = Boundaries()
    private val space = HashMap<Pair<Int, Int>, Char>()

    fun findAreaWithinXToLocations(maxDistanceSum: Int): Int? {
        val coordinates = HashMap<Char, Pair<Int, Int>>()
        var locationName = 'a'
        val locations = HashMap<Pair<Int, Int>, Char>()
        val coordinatesList = ArrayList<Pair<Int, Int>>()

        coordinatesRaw.forEach { line ->
            val xy = line.split(", ")
            val newLocation = Pair(xy[0].toInt(), xy[1].toInt())
            coordinates[locationName] = newLocation
            locations[newLocation] = locationName.toUpperCase()
            locationName++
            coordinatesList.add(newLocation)
        }

        calculateBoundaries(coordinates)

        var hadRowWithArea = false
        for (y in boundaries.top .. boundaries.bottom) {
            var rowWithArea = false
            for (x in boundaries.left .. boundaries.right) {
                val currentLocation = Pair(x, y)

                val distancesSum = coordinatesList.stream().mapToInt() { location ->
                    currentLocation.calculateManhattanDistanceTo(location)
                }.sum()

                if (distancesSum < maxDistanceSum) {
                    space[currentLocation] = '#'
                    hadRowWithArea = true
                    rowWithArea = true
                    if (x == boundaries.left || x == boundaries.right) {
                        println("We are at the border! x = $x, y = $y")
                    }
                    if (y == boundaries.top || y == boundaries.bottom) {
                        println("Wa are at the top/bottom! x = $x, y = $y")
                    }
                } else {
                    space[currentLocation] = '.'
                }
            }

            if (hadRowWithArea && !rowWithArea) {
                break
            }
        }

        return space.values.count { it == '#' }
    }

    fun findLargestSeparateArea(): Int? {
        val coordinates = HashMap<Char, Pair<Int, Int>>()
        var locationName = 'a'
        val locations = HashMap<Pair<Int, Int>, Char>()

        coordinatesRaw.forEach { line ->
           val xy = line.split(", ")
            val newLocation = Pair(xy[0].toInt(), xy[1].toInt())
            coordinates[locationName] = newLocation
            space[newLocation] = locationName
            locations[newLocation] = locationName.toUpperCase()
            locationName++
        }

        calculateBoundaries(coordinates)

        coordinates.forEach {_, coordinate ->
            if (!coordinate.isOnBorder()) {
                coordinate.crawlAround(coordinates)
            }
        }

        val areas = HashMap<Char, Int>()

//        for (y in boundaries.top .. boundaries.bottom) {
//            for (x in boundaries.left .. boundaries.right) {
//                val coord = Pair(x, y)
//                var toPrint = ' '
//                Optional.ofNullable(space[coord]).ifPresent { toPrint = it }
//                Optional.ofNullable(locations[coord]).ifPresent { toPrint = it }
//                print(toPrint)
//            }
//            println()
//        }

        space.values.forEach { value ->
            areas[value] = Optional.ofNullable(areas[value]).orElse(0) + 1
        }

        return areas.values.max()
    }

    private fun Pair<Int, Int>.crawlAround(coordinates: HashMap<Char, Pair<Int, Int>>) {
        var ring = 0
        var iWasTheClosestInTheRing = true
        var continueCrawl = true
        var currentPosition = goLeft()
        var previousMove = "left"

        do {
            if (currentPosition.isBeyondBoundaries()) {
                break
            }

            if (space[currentPosition] == null) {
                val closestLocation = coordinates.findClosestLocationTo(currentPosition)
                space[currentPosition] = closestLocation
            }

            val nextMove = currentPosition.decideNextMove(previousMove, this, ring)
            if (previousMove == "left" && nextMove == "up") {
                if (!iWasTheClosestInTheRing) {
                    continueCrawl = false
                    continue
                }
                ring++
                iWasTheClosestInTheRing = false
            }

            if (space[currentPosition] == space[this]) {
                iWasTheClosestInTheRing = true
            }

            currentPosition = currentPosition.doNextMove(nextMove)
            previousMove = nextMove

        } while (continueCrawl)
    }

    private fun Pair<Int, Int>.decideNextMove(previousMove: String, location: Pair<Int, Int>, ring: Int): String {
        when(previousMove) {
            "up" -> {
                if (second == location.second - ring) {
                    return "right"
                }
                return "up"
            }
            "down" -> {
                if (second == location.second + ring) {
                    return "left"
                }
                return "down"
            }
            "left" -> {
                if (first == location.first - ring - 1) {
                    return "up"
                }
                return "left"
            }
            "right" -> {
                if (first == location.first + ring) {
                    return "down"
                }
                return "right"
            }
        }
        return previousMove
    }

    private fun Pair<Int, Int>.doNextMove(move: String): Pair<Int, Int> {
        when(move) {
            "up" -> {
                return goUp()
            }
            "down" -> {
                return goDown()
            }
            "left" -> {
                return goLeft()
            }
            "right" -> {
                return goRight()
            }
        }
        return this
    }

    private fun HashMap<Char, Pair<Int, Int>>.findClosestLocationTo(location: Pair<Int, Int>): Char {
        var minDistance = 999_999
        var minDistanceLocation = ' '


        forEach{locationName, coordinate ->
            if (coordinate != location) {
                val distance = coordinate.calculateManhattanDistanceTo(location)
                when {
                    distance < minDistance -> {
                        minDistance = distance
                        minDistanceLocation = locationName
                    }
                    distance == minDistance -> {
                       minDistanceLocation = '.'
                    }
                }
            }
        }

        return minDistanceLocation
    }

    private fun Pair<Int, Int>.calculateManhattanDistanceTo(other: Pair<Int, Int>): Int {
        return (first - other.first).absoluteValue + (second - other.second).absoluteValue
    }
    private fun calculateBoundaries(coordinates: Map<Char, Pair<Int, Int>>) {
        coordinates.values.forEach {
            when {
                it.first < boundaries.left -> {
                    boundaries.left = it.first
                }
                it.first > boundaries.right -> {
                    boundaries.right = it.first
                }
            }

            when {
                it.second < boundaries.top -> {
                    boundaries.top = it.second
                }
                it.second > boundaries.bottom -> {
                    boundaries.bottom = it.second
                }
            }
        }
    }

    private fun Pair<Int, Int>.isOnBorder(): Boolean {
        if (first == boundaries.left || first == boundaries.right) {
            return true
        }

        if (second == boundaries.top || second == boundaries.bottom) {
            return true
        }

        return false
    }

    private fun Pair<Int, Int>.isBeyondBoundaries(): Boolean {
        if (first < boundaries.left || first > boundaries.right || second > boundaries.bottom || second < boundaries.top) {
            return true
        }

        return false
    }
}

fun Pair<Int, Int>.goUp(): Pair<Int, Int> {
    return Pair(first, second - 1)
}

fun Pair<Int, Int>.goDown(): Pair<Int, Int> {
    return Pair(first, second + 1)
}

fun Pair<Int, Int>.goLeft(): Pair<Int, Int> {
    return Pair(first - 1, second)
}

fun Pair<Int, Int>.goRight(): Pair<Int, Int> {
    return Pair(first + 1, second)
}

data class Boundaries(
        var top: Int = 999_999,
        var left: Int = 999_999,
        var bottom: Int = 0,
        var right: Int = 0
)
// (5,2)
//  (1, 1) -> 5
//  (3, 4) -> 4
//  (5, 5) -> 3
//  (8, 3) -> 4
/*
A
aadd.ccc
addd.ccC
.dD.e.cc
b.deE.ec
Bb....e.
 b....ff
bb....ff
       F



A
aaddeccc
adddeccC
.dDdeecc
b.deEeec
Bb.eeee.
bb.eeeff
bb.eefff
       F
 */