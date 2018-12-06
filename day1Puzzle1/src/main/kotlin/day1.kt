package day1Puzzle1

import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {

    val calculator = FrequencyCalculator()
    val file = calculator.getFile()
//    println(calculator.calculate(file))
    println(calculator.calibrate(file))

}
// 77666

class FrequencyCalculator {

    fun getFile(): File {
        return File(Paths.get(FrequencyCalculator::class.java.classLoader.getResource("input").toURI()).toString())
    }


    fun calculate(file: File): Long {
        var frequency = 0L

        file.forEachLine {
            val parsed = it.toLong()
            frequency += parsed
        }

        return frequency
    }

    fun calibrate(file: File): Long {
        var frequency = 0L
        var foundFrequency = 0L
        val seenFrequencies = ArrayList<Long>()

        println("Starting to calibrate")

        val lines = file.readLines()
        do {
            lines.forEach {
                if (foundFrequency == 0L) {
                    val parsed = it.toLong()
                    frequency += parsed
                    if (seenFrequencies.contains(frequency)) {
                        foundFrequency = frequency
                    }
                    seenFrequencies.add(frequency)
                    if (seenFrequencies.size % 10 == 0) {
                        print(".")
                    }
                }
            }

            println()
            println("Seen ${seenFrequencies.size} different frequencies and still searching...")
        } while (foundFrequency == 0L)

        return foundFrequency
    }
}