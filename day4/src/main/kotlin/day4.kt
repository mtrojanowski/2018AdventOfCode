import day1Puzzle1.FrequencyCalculator

fun main(args: Array<String>) {
    val lines = FrequencyCalculator().getFile().readLines()
    val analyzer = GuardAnalyzer(lines)

//    println("Starting to analyze guard data")
//    val result = analyzer.analyzeGuardSleepines()

    println("Starting to analyze guard data with strategy 2")
    val result = analyzer.analyzeGuardSleepinesWithStrategy2()

    println(result)
}

class GuardAnalyzer(private val lines: List<String>) {
    private val pattern = "\\[(.*)\\].*".toRegex()
    private val shiftPattern = "\\[15\\d\\d-(\\d+-\\d+) \\d{2}:\\d{2}\\] Guard #(\\d+) begins shift".toRegex()
    private val wakesUpPattern = "\\[15\\d\\d-\\d+-\\d+ \\d{2}:(\\d{2})\\] wakes up".toRegex()
    private val fallsAsleepPattern = "\\[15\\d\\d-(\\d+-\\d+) \\d{2}:(\\d{2})\\] falls asleep".toRegex()

    fun analyzeGuardSleepines(): Int {
        val preparedData = ArrayList<GuardData>()

        val sortedLines = lines.map {
            val matched = pattern.matchEntire(it)
            Pair(matched!!.groupValues[1], it)
        }.sortedBy { it.first }

        sortedLines.forEach {line ->
            when {
                line.second.contains("Guard") -> {
                    val parsed = shiftPattern.matchEntire(line.second)
                    preparedData.add(GuardData("", parsed!!.groupValues[2].toInt(), ArrayList()))
                }
                line.second.contains("wakes") -> {
                    val guardData = preparedData.last()
                    val parsed = wakesUpPattern.matchEntire(line.second)
                    val sleepyTime = guardData.sleepyTimes.last()
                    sleepyTime.wakesUpAtMinute = parsed!!.groupValues[1].toInt()
                }
                line.second.contains("falls") -> {
                    val guardData = preparedData.last()
                    val parsed = fallsAsleepPattern.matchEntire(line.second)
                    guardData.monthDay = parsed!!.groupValues[1]
                    guardData.sleepyTimes.add(SleepyTime(parsed.groupValues[2].toInt(), 0))
                }
            }
        }

        val guards = HashMap<Int, GuardInfo>()
        preparedData.stream().forEach {
           val guard = guards[it.guardId] ?: GuardInfo(it.guardId, 0, getAMap())

            it.sleepyTimes.forEach {sleepyTime ->
                val timeAsleep = sleepyTime.wakesUpAtMinute - sleepyTime.fallsAsleepAtMinute
                for (i in sleepyTime.fallsAsleepAtMinute until sleepyTime.wakesUpAtMinute) {
                    guard.datesMatrix[i] = guard.datesMatrix[i]!! + 1
                }
                guard.timeAsleep += timeAsleep
            }

            guards[it.guardId] = guard

        }

        val longestSleepingGuard = guards.values.stream().max { o1, o2 -> if (o1.timeAsleep > o2.timeAsleep) 1 else-1 }.get()

        var mostMinutesAsleep = 0
        var theMostFrequentMinuteAsleep = 0

        longestSleepingGuard.datesMatrix.forEach { (minute, asleep) ->
            if (asleep > mostMinutesAsleep) {
                mostMinutesAsleep = asleep
                theMostFrequentMinuteAsleep = minute
            }
        }

        println("Longest sleeping guard is #${longestSleepingGuard.id} and he slept $mostMinutesAsleep times on minute $theMostFrequentMinuteAsleep")


        return theMostFrequentMinuteAsleep * longestSleepingGuard.id
    }

    fun analyzeGuardSleepinesWithStrategy2(): Int {
        val preparedData = ArrayList<GuardData>()

        val sortedLines = lines.map {
            val matched = pattern.matchEntire(it)
            Pair(matched!!.groupValues[1], it)
        }.sortedBy { it.first }

        sortedLines.forEach {line ->
            when {
                line.second.contains("Guard") -> {
                    val parsed = shiftPattern.matchEntire(line.second)
                    preparedData.add(GuardData("", parsed!!.groupValues[2].toInt(), ArrayList()))
                }
                line.second.contains("wakes") -> {
                    val guardData = preparedData.last()
                    val parsed = wakesUpPattern.matchEntire(line.second)
                    val sleepyTime = guardData.sleepyTimes.last()
                    sleepyTime.wakesUpAtMinute = parsed!!.groupValues[1].toInt()
                }
                line.second.contains("falls") -> {
                    val guardData = preparedData.last()
                    val parsed = fallsAsleepPattern.matchEntire(line.second)
                    guardData.monthDay = parsed!!.groupValues[1]
                    guardData.sleepyTimes.add(SleepyTime(parsed.groupValues[2].toInt(), 0))
                }
            }
        }

        val guards = HashMap<Int, GuardInfo>()
        preparedData.stream().forEach {
            val guard = guards[it.guardId] ?: GuardInfo(it.guardId, 0, getAMap())

            it.sleepyTimes.forEach {sleepyTime ->
                val timeAsleep = sleepyTime.wakesUpAtMinute - sleepyTime.fallsAsleepAtMinute
                for (i in sleepyTime.fallsAsleepAtMinute until sleepyTime.wakesUpAtMinute) {
                    guard.datesMatrix[i] = guard.datesMatrix[i]!! + 1
                }
                guard.timeAsleep += timeAsleep
            }

            guards[it.guardId] = guard

        }

        var mostMinutesAsleep = 0
        var theMostFrequentMinuteAsleep = 0
        var guardWithMostFrequentMinuteAsleep = 0

        guards.values.stream().forEach {
            it.datesMatrix.forEach { (minute, asleep) ->
            if (asleep > mostMinutesAsleep) {
                mostMinutesAsleep = asleep
                theMostFrequentMinuteAsleep = minute
                guardWithMostFrequentMinuteAsleep = it.id
            }
        }
        }

        println("Guard with a most frequest asleep minute is #${guardWithMostFrequentMinuteAsleep} and he slept $mostMinutesAsleep times on minute $theMostFrequentMinuteAsleep")

        return theMostFrequentMinuteAsleep * guardWithMostFrequentMinuteAsleep
    }


    private fun getAMap(): HashMap<Int, Int> {
        val result = HashMap<Int, Int>(60)
        for (i in 0 .. 59) {
            result[i] = 0
        }

        return result
    }
}

data class GuardData(
        var monthDay: String,
        val guardId: Int,
        val sleepyTimes: ArrayList<SleepyTime>
)

data class SleepyTime(
        var fallsAsleepAtMinute: Int,
        var wakesUpAtMinute: Int
)

data class GuardInfo(
        val id: Int,
        var timeAsleep: Int,
        var datesMatrix: HashMap<Int, Int>
)