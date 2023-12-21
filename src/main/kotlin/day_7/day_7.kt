package day_7

import Day

enum class Type {
    FIVE_OF_KIND, FOUR_OF_KIND, FULL_HOUSE, THREE_OF_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD
}

class Day7 : Day() {
    override fun partA() = day7a()

    override fun partB() = day7b()
}
