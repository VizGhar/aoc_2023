package day_5

import day_5.Day5.*

private fun executeIteration(ranges: List<LongRange>, mapping: List<Mapping>) : List<LongRange> {
    val rangesSorted = ranges.sortedBy { it.first }
    // there are multiple $ranges in which seeds are currently stored
    // there are multiple $mapping ranges that maps seeds from $source to $destination
    // example 1-12 SOURCE 4-15 destionation means seed on place 2 (SOURCE) will be stored in 5 (DESTINATION)

    return rangesSorted.map { range ->

        // currently solved range can be split into multiple minor splits stored here
        val newSplits = mutableListOf<LongRange>()

        var solving = range.first

        while (solving < range.last) {
            // find suitable mapping for currently solved seed position
            val foundMapping = mapping.first { it.intersects(solving) }
            val delta = solving - foundMapping.source.first
            val split = solving..minOf(foundMapping.source.last, range.last)

            // store new split
            newSplits += (foundMapping.destination.first + delta) until (foundMapping.destination.first + delta + split.count())

            // and find next position
            solving = split.last + 1
        }

        newSplits
    }.flatten()
}

fun Day5.day5b(): String {

    val seedRanges: List<LongRange> = input[0].substringAfter(": ").split(" ").map { it.toLong() }.chunked(2).map { it[0] until it[0] + it[1] }
    val mappings = getMapping()

    var currentSeedRanges = seedRanges
    for (mapping in mappings) {
        val sorted = mapping.sortedBy { it.source.first }
        currentSeedRanges = executeIteration(currentSeedRanges, sorted)
    }

    return currentSeedRanges.first().first.toString()
}