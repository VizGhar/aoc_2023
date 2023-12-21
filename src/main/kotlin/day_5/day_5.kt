package day_5

import Day

class Day5 : Day() {

    data class Header(val sourceCategory: String, val destinationCategory: String)
    data class Mapping(val source: LongRange, val destination: LongRange) { fun intersects(target: Long) = target in source }

    fun getMapping(): List<List<Mapping>> {
        val mappings = mutableListOf<List<Mapping>>()
        var header: Header? = null  // not important
        var currentRanges = mutableListOf<Mapping>()

        for (i in 2..<input.size) {
            val line = input[i]

            fun saveMapping() { mappings += currentRanges; header = null; currentRanges = mutableListOf() }

            // parsing of new mapping started -> parse header
            if (header == null) {
                line.substringBefore(" ").split("-to-").let { header = Header(it[0], it[1]) }
                continue
            }

            // parsing of new mapping finished (empty line) -> store and continue
            if (line.isEmpty()) { saveMapping(); continue }

            // parsing in progress
            line.split(" ").let {
                val destinationStartIndex = it[0].toLong()
                val sourceStartIndex = it[1].toLong()
                val length = it[2].toLong()
                currentRanges += Mapping(sourceStartIndex until sourceStartIndex + length, destinationStartIndex until destinationStartIndex + length)
            }

            // last line also stores inputs
            if (i == input.size - 1) { saveMapping() }
        }

        // generate not mentioned mappings for example 0..100 -> 0..100 (same ranges for both source and destination)
        val complete = mappings.map { mapping ->
            val sortedMappings = mapping.sortedBy { it.source.first }
            val allMappings = mutableListOf<Mapping>()

            val first = 0..<sortedMappings[0].source.first
            if (!first.isEmpty()) allMappings += Mapping(first, first)
            for (i in 0..<sortedMappings.size - 1) {
                allMappings += sortedMappings[i]
                val next = (sortedMappings[i].source.last + 1)..<sortedMappings[i + 1].source.first
                if (!next.isEmpty()) allMappings += Mapping(next, next)
            }
            allMappings += sortedMappings.last()
            val last = (sortedMappings.last().source.last + 1) .. Long.MAX_VALUE
            if (!last.isEmpty()) allMappings += Mapping(last, last)
            allMappings
        }

        return complete
    }

    override fun partA() = day5a()

    override fun partB() = day5b()
}
