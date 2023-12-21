package day_19

import day_18.Day19

@Suppress("EnumEntryName")
private enum class Symbol { x, m, a, s }

private data class Split(
    val symbol     : Symbol,
    val lower      : Boolean,
    val value      : Int,
    val targetLabel: String
)

private data class Instruction(
    val label         : String,
    val splits        : List<Split>,
    val remainderLabel: String
)

private data class Range(
    val from: Int = 1,
    val to  : Int = 4000
) {
    val size : Long = to - from + 1L
}

private data class Ranges(
    val x: Range = Range(),
    val m: Range = Range(),
    val a: Range = Range(),
    val s: Range = Range(),
) {
    operator fun get(symbol: Symbol) = when (symbol) {
        Symbol.x -> x
        Symbol.m -> m
        Symbol.a -> a
        Symbol.s -> s
    }

    val weight = x.size * m.size * a.size * s.size
}

private fun solve(instructions: List<Instruction>, label: String, ranges: Ranges): Long {
    if (label == "A") return ranges.weight
    if (label == "R") return 0L

    val solving = instructions.first { it.label == label }
    var actualRange = ranges

    val newRanges = mutableListOf<Pair<Ranges, String>>()
    solving.splits.forEach { (symbol, lower, value, target) ->

        if (lower) {
            val lowerRange = Ranges(
                x = if (symbol == Symbol.x) Range(actualRange.x.from, value - 1) else actualRange.x,
                m = if (symbol == Symbol.m) Range(actualRange.m.from, value - 1) else actualRange.m,
                a = if (symbol == Symbol.a) Range(actualRange.a.from, value - 1) else actualRange.a,
                s = if (symbol == Symbol.s) Range(actualRange.s.from, value - 1) else actualRange.s
            )

            val higherRange = Ranges(
                x = if (symbol == Symbol.x) Range(value, actualRange.x.to) else actualRange.x,
                m = if (symbol == Symbol.m) Range(value, actualRange.m.to) else actualRange.m,
                a = if (symbol == Symbol.a) Range(value, actualRange.a.to) else actualRange.a,
                s = if (symbol == Symbol.s) Range(value, actualRange.s.to) else actualRange.s
            )
            newRanges += lowerRange to target
            actualRange = higherRange
        } else {
            val lowerRange = Ranges(
                x = if (symbol == Symbol.x) Range(actualRange.x.from, value) else actualRange.x,
                m = if (symbol == Symbol.m) Range(actualRange.m.from, value) else actualRange.m,
                a = if (symbol == Symbol.a) Range(actualRange.a.from, value) else actualRange.a,
                s = if (symbol == Symbol.s) Range(actualRange.s.from, value) else actualRange.s
            )

            val higherRange = Ranges(
                x = if (symbol == Symbol.x) Range(value + 1, actualRange.x.to) else actualRange.x,
                m = if (symbol == Symbol.m) Range(value + 1, actualRange.m.to) else actualRange.m,
                a = if (symbol == Symbol.a) Range(value + 1, actualRange.a.to) else actualRange.a,
                s = if (symbol == Symbol.s) Range(value + 1, actualRange.s.to) else actualRange.s
            )
            newRanges += higherRange to target
            actualRange = lowerRange
        }
    }

    newRanges += actualRange to solving.remainderLabel
    val result = newRanges.sumOf { (range, label) ->
        solve(instructions, label, range)
    }
    return result
}

fun Day19.part2(): String {
    val instructions = input.takeWhile { it.isNotEmpty() }.map { line ->
        val name = line.substringBefore("{")
        val s = line.substringAfter("{").substringBefore("}").split(",")
        val remainder = s.last()
        val splits = s.dropLast(1).map {
            Split(
                symbol      = Symbol.valueOf(it[0].toString()),
                lower       = it[1] == '<',
                value       = it.substring(2).substringBefore(":").toInt(),
                targetLabel = it.substringAfter(":")
            )
        }
        Instruction(
            label          = name,
            splits         = splits,
            remainderLabel = remainder
        )
    }.toMutableList()

    return solve(instructions, "in", Ranges()).toString()
}