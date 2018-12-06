import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.math.absoluteValue

fun main(args: Array<String>) {

    val polymer = Loader.getFile("day5Input").readText()
    val shrinker = PolymerShrinker(polymer)

//    println("About to shrink the polymer!")
//    println("The original polymer is ${polymer.length} characters long")
//    val result = shrinker.shrink()
//
//    println("The shrunk polymer is ${result.length} characters long")

    println("About to shrink the polymer even better!")
    println("The original polymer is ${polymer.length} characters long")
    val result = shrinker.shrinkEvenBetter()

    println("The shrunk polymer is ${result.length} characters long")
}

class Loader {
    companion object {
        fun getFile(filename: String): File {
            return File(Paths.get(this::class.java.classLoader.getResource(filename).toURI()).toString())
        }
    }
}

class PolymerShrinker(private val polymer: String) {
    fun shrink(): String {

        var shrunkPolymer = polymer

        var i = 0


        do {
            if ((shrunkPolymer[i].toByte() - shrunkPolymer[i + 1].toByte()).absoluteValue == 32) {
                shrunkPolymer = shrunkPolymer.removeRange(i, i + 2)
                i = if (i < 1)  i else i -1
            } else {
                i++
            }
        } while (i < shrunkPolymer.length - 1)

        return shrunkPolymer
    }

    fun shrinkEvenBetter(): String {
        val units = HashSet<Char>()
        val unitsCount = HashMap<Char, Int>()

        polymer.forEach {
            units.add(it.toLowerCase())
            unitsCount[it.toLowerCase()] = Optional.ofNullable(unitsCount[it.toLowerCase()]).orElse(0) + 1
        }

        val shrunkPolymers = ArrayList<String>()
        units.stream().forEach { unit ->
            val polymerWithoutAUnit = polymer.filter { c -> c.toLowerCase() != unit }
            var shrunkPolymer = polymerWithoutAUnit
            var i = 0

            do {
                units.add(shrunkPolymer[i].toLowerCase())

                if ((shrunkPolymer[i].toByte() - shrunkPolymer[i + 1].toByte()).absoluteValue == 32) {
                    shrunkPolymer = shrunkPolymer.removeRange(i, i + 2)
                    i = if (i < 1)  i else i -1
                } else {
                    i++
                }
            } while (i < shrunkPolymer.length - 1)

            shrunkPolymers.add(shrunkPolymer)
        }

        val maybeMinimalPolymer = shrunkPolymers.stream().min { o1, o2 -> if (o1.length > o2.length) 1 else -1 }

        return maybeMinimalPolymer!!.get()
    }
}

// 4952