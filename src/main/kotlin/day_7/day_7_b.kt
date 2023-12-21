package day_7

val cardSymbols = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J') // card names - sorted by score
val cardSymbolsNoJ = cardSymbols - 'J' // without J - for alternatives generation

data class Hand(val cards: String): Comparable<Hand> {

    private val type: Type

    init {
        var bestAlternative = this

        do {
            val jokers = bestAlternative.cards.count { it == 'J' }
            if (jokers == 0) break

            // always replace last joker -> hunt for highest score
            val lastJ = bestAlternative.cards.indexOfLast { it == 'J' }

            val bestAlternativeCandidate = cardSymbolsNoJ.maxOf { cardName ->
                Hand(bestAlternative.cards.mapIndexed { index, c -> if (index == lastJ) cardName else c }.joinToString(""))
            }

            if (bestAlternative == bestAlternativeCandidate) break
            bestAlternative = bestAlternativeCandidate
        } while(true)

        type = with(bestAlternative.cards.groupBy { it }) {
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
    }

    override fun compareTo(other: Hand): Int {
        // check by type
        if (this.type.ordinal != other.type.ordinal) return other.type.ordinal - this.type.ordinal
        // else check by first distinct char
        for (c in this.cards.zip(other.cards)) {
            if (c.first != c.second) {
                return cardSymbols.indexOf(c.second) - cardSymbols.indexOf(c.first)
            }
        }
        return 0
    }
}

data class Play(val hand: Hand, val bid: Int)

fun Day7.day7b() : String {
    val plays = input.map { Play(Hand(it.substringBefore(" ")), it.substringAfter(" ").toInt()) }
    val sorted = plays.sortedBy { it.hand }
    return sorted.mapIndexed { index, play -> (index + 1) * play.bid }.sum().toString()
}