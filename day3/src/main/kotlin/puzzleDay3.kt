import day1Puzzle1.FrequencyCalculator
import java.util.stream.IntStream

fun main(args: Array<String>) {
    val lines = FrequencyCalculator().getFile().readLines()
    val calculator = FabricCalculator(lines)

//    println("Calculating overlapping area")
//    val result = calculator.calculateOverlappingSquares()
    println("Find non overlapping claim")
    val result = calculator.findNotOverlappingClaim()

    println(result)
}

class FabricCalculator(val lines: List<String>) {
    private val fabric = prepareMatrix()
    private val linePattern = "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)"

    private fun prepareMatrix(): HashMap<Pair<Int, Int>, Int> {
        val result = HashMap<Pair<Int, Int>, Int>()
        IntStream.range(0, 1000).forEach { x ->
            IntStream.range(0, 1000).forEach { y ->
                result[Pair(x, y)] = 0
            }
        }

        return result
    }

    fun calculateOverlappingSquares(): Long {
        lines.forEach { line ->
            val matchResult = linePattern.toRegex().find(line)
            val ( _, fromLeft, fromTop, width, height ) = matchResult!!.destructured

            for (x in fromLeft.toInt() until fromLeft.toInt() + width.toInt()) {
                for (y in fromTop.toInt() until fromTop.toInt() + height.toInt()) {
                    fabric[Pair(x, y)] = fabric[Pair(x, y)]!! + 1
                }
            }
        }

        return fabric.values.stream().filter { it > 1 }.count()
    }

    fun findNotOverlappingClaim(): String {
        lines.forEach { line ->
            val matchResult = linePattern.toRegex().find(line)
            val ( _, fromLeft, fromTop, width, height ) = matchResult!!.destructured

            for (x in fromLeft.toInt() until fromLeft.toInt() + width.toInt()) {
                for (y in fromTop.toInt() until fromTop.toInt() + height.toInt()) {
                    fabric[Pair(x, y)] = fabric[Pair(x, y)]!! + 1
                }
            }
        }

        var nonOverlappingId = ""

        lines.forEach { line ->
            if (nonOverlappingId == "") {
                val matchResult = linePattern.toRegex().find(line)
                val (id, fromLeft, fromTop, width, height) = matchResult!!.destructured

                var hasOverlappingFragments = false
                for (x in fromLeft.toInt() until fromLeft.toInt() + width.toInt()) {
                    for (y in fromTop.toInt() until fromTop.toInt() + height.toInt()) {
                        if (fabric[Pair(x, y)]!! > 1) {
                            hasOverlappingFragments = true
                        }
                    }
                }

                if (!hasOverlappingFragments) {
                    nonOverlappingId = id
                }
            }
        }

        return nonOverlappingId
    }
}