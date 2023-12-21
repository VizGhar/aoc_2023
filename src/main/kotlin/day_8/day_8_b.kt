package day_8

fun Day8.day8b() : String {
    val instructions = input[0].toCharArray()
    val map = input.drop(2).map {
        Instruction(it.substringBefore(" = "), it.substringAfter("(").substringBefore(","), it.substringAfter(", ").substringBefore(")"))
    }

    val positions = map.filter { it.source.endsWith("A") }.map{ it.source }.toMutableList()

    // find cycles
    val cycles = positions.map { p ->
        var position = p
        var instructionId = 0
        val foundTargets = mutableMapOf<String, Int>()
        while (!foundTargets.containsKey(position)) {
            if (position.endsWith("Z")) { foundTargets[position] = instructionId }
            val instruction = instructions[instructionId % instructions.size]
            position = map.first { route -> route.source == position }.let { route -> if (instruction == 'L') route.left else route.right }
            instructionId += 1
        }
        foundTargets
    }

    val simplify = cycles.map { it.entries.first().value }.sorted()
    val smallest = simplify[0]
    var result = smallest.toLong()
    while (!simplify.all { result % it  == 0L }) {
        result+=smallest
    }

    return result.toString()
}