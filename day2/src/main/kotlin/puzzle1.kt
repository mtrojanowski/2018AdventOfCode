import day1Puzzle1.FrequencyCalculator
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val file = FrequencyCalculator().getFile()
    val puzzle = Puzzle1()

//    val result = puzzle.calculateCheckSum(file)
//    println("Counting checksum...")

    println("Finding closest difference")

    val result = puzzle.getCommonLettersOfClosestBoxes(file)
    println(result)
}

// mphcuasvrnjzzakbgdtqeoylva
// mphcuasvrnjzzwkbgdtqeoylva
// mphcuasvrnjzzkbgdtqeoylva

class Puzzle1 {
    fun calculateCheckSum(input: File): Int {
        val lines = input.readLines()
        var numberOfTwoLetters = 0
        var numberOfThreeLetters = 0

        lines.forEach {
            val countChars = hashMapOf<Char, Int>()
            it.forEach { char ->
                val charCount = Optional.ofNullable(countChars[char]).orElse(0)
                countChars[char] = charCount + 1
            }

            if (countChars.values.stream().filter {value -> value == 2}.count() > 0) {
                numberOfTwoLetters += 1
            }

            if (countChars.values.stream().filter {value -> value == 3}.count() > 0) {
                numberOfThreeLetters += 1
            }
        }

        return numberOfThreeLetters * numberOfTwoLetters
    }

    fun getCommonLettersOfClosestBoxes(input: File): String {

        val lines = input.readLines()
        val boxDifferences = hashMapOf<Pair<String, String>, Int>()

        lines.forEach { id1 ->
            lines.forEach { id2 ->
                if (id1 != id2 && boxDifferences[Pair(id2, id1)] == null) {
                    var numberOfDifferentCharacters = 0
                    for (i in id1.indices) {
                        if (id1[i] != id2[i]) {
                            numberOfDifferentCharacters++
                        }
                    }
                    boxDifferences[Pair(id1, id2)] = numberOfDifferentCharacters
                }

            }
        }

        var finalPairOfBoxes = Pair("", "")
        var currentLowestDifference = 999_999
        boxDifferences.forEach { pairOfBoxes, numberOfDifferentCharacters ->
            if (numberOfDifferentCharacters < currentLowestDifference) {
                currentLowestDifference = numberOfDifferentCharacters
                finalPairOfBoxes = pairOfBoxes
            }
        }

        var finalDifference = ""
        val box1 = finalPairOfBoxes.first
        val box2 = finalPairOfBoxes.second

        println("Final pair of boxes: $box1 and $box2 have difference of $currentLowestDifference")

        for (i in box1.indices) {
            if (box1[i] == box2[i]) {
                finalDifference += box1[i]
            }
        }

        return finalDifference
    }
}
