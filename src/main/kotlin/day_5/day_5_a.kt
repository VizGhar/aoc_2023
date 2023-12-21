package day_5

fun Day5.day5a(): String {
    val seeds: List<Long> = input[0].substringAfter(": ").split(" ").map { it.toLong() }
    val mappings = getMapping()

    val result = seeds.map {
        var location = it
        for (mapping in mappings) {
            val actual = mapping.first { it.source.contains(location) }
            val offset = location - actual.source.first
            location = actual.destination.first + offset
        }
        location
    }.min()


    return result.toString()
}