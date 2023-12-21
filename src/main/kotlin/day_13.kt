class Day13: Day() {

    private fun validate(mirrors: List<Int>, map: List<String>) = mirrors.filter { mirror ->
        var i = 0
        while (mirror - i >= 0 && mirror + i + 1 < map.size) {
            if (map[mirror - i] != map[mirror + i + 1]) { return@filter false}
            i++
        }
        true
    }.map { it + 1 }

    private data class Solution(val horizontal: Int?, val vertical: Int?) {
        val score = horizontal?.let { it * 100 } ?: vertical ?: 0
    }

    private fun solution(map: List<String>, ignore: Solution? = null) : Solution? {
        val swapMap = List(map[0].length) { i -> map.map { it[i] }.joinToString("") }
        val pairsHorizontalLine = map.windowed(2).mapIndexedNotNull { index, strings -> if (strings[0] == strings[1]) index else null }
        val pairsVerticalLine = swapMap.windowed(2).mapIndexedNotNull { index, strings -> if (strings[0] == strings[1]) index else null }

        // validation
        val validHorizontal = validate(pairsHorizontalLine, map).filter { it != ignore?.horizontal }
        val validVertical = validate(pairsVerticalLine, swapMap).filter { it != ignore?.vertical }

        if (validHorizontal.isNotEmpty()) return Solution(validHorizontal[0], null)
        if (validVertical.isNotEmpty()) return Solution(null, validVertical[0])
        return null
    }

    private fun parse(): MutableList<List<String>> {
        val maps = mutableListOf<List<String>>()
        val actualMap = mutableListOf<String>()
        for (line in input) {
            if (line.isEmpty()) { maps += actualMap.toList(); actualMap.clear() }
            else { actualMap += line }
        }
        maps += actualMap
        return maps
    }

    override fun partA() = parse().sumOf { solution(it)?.score ?: 0 }.toString()

    override fun partB(): String {
        val maps = parse()

        val result = maps.sumOf { mapBase ->
            val allMaps = mutableListOf<List<String>>()
            for (y in mapBase.indices) {
                for (x in 0 until mapBase[0].length) {
                    allMaps += mapBase.mapIndexed { mapY, s -> if (mapY == y) s.replaceRange(x..x, if (s[x] == '#') "." else "#") else s }
                }
            }

            val oldSolution = solution(mapBase)

            val newSolutions = allMaps
                .mapNotNull { solution(it, oldSolution) }
                .firstOrNull { it.score > 0 && it != oldSolution }

            newSolutions?.score ?: 0
        }
        return result.toString()
    }

}