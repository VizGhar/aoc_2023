package day_8

data class Instruction(val source: String, val left: String, val right: String)

fun Day8.day8a() : String {
    val instructions = input[0].toCharArray()
    val map = input.drop(2).map {
        Instruction(it.substringBefore(" = "), it.substringAfter("(").substringBefore(","), it.substringAfter(", ").substringBefore(")"))
    }

    var position = "AAA"
    var instructionId = 0
    while (position != "ZZZ") {
        val instruction = instructions[instructionId % instructions.size]
        position = map.first { route -> route.source == position }.let { route -> if(instruction=='L') route.left else route.right }
        instructionId += 1
    }
    return instructionId.toString()
}