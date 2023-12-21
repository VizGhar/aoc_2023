package day_7

val cardNames = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')

data class HandA(val cards: String): Comparable<HandA> {
    val type : Type =
        with(cards.groupBy { it }) {
            when {
                size == 1 -> Type.FIVE_OF_KIND
                any { it.value.size == 4 } -> Type.FOUR_OF_KIND
                size == 2 -> Type.FULL_HOUSE
                any { it.value.size == 3 } -> Type.THREE_OF_KIND
                count { it.value.size == 2 } == 2 -> Type.TWO_PAIR
                size == 4 -> Type.ONE_PAIR
                else -> Type.HIGH_CARD
            }
        }

    override fun compareTo(other: HandA): Int {
        // check by type
        if (this.type.ordinal != other.type.ordinal) return other.type.ordinal - this.type.ordinal
        // else check by first distinct char
        for (c in this.cards.zip(other.cards)) {
            if (c.first != c.second) {
                return cardNames.indexOf(c.second) - cardNames.indexOf(c.first)
            }
        }
        return 0
    }
}

data class PlayA(val hand: HandA, val bid: Int)

fun Day7.day7a() : String {
    val plays = input.map { PlayA(HandA(it.substringBefore(" ")), it.substringAfter(" ").toInt()) }
    val sorted = plays.sortedBy { it.hand }
    return sorted.mapIndexed { index, play -> (index + 1) * play.bid }.sum().toString()
}