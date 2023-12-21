package day_9

fun Day9.day9a() : String {
    val result = input.sumOf { line ->
        val lineSequence = line.split(" ").map { it.toInt() }
        var actualSequence = lineSequence
        val tree = mutableListOf(actualSequence)
        while (!actualSequence.all { it == 0 }) {
            actualSequence = actualSequence.windowed(2).map { it[1] - it[0] }
            tree += actualSequence
        }

        var result = 0
        for (i in tree.size - 1 downTo 0) {
            result += tree[i].last()
        }
        result
    }
    return result.toString()
}