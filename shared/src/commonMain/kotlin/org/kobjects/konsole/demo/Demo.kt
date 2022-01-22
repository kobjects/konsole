package org.kobjects.konsole.demo

import org.kobjects.konsole.demo.banner.banner
import org.kobjects.konsole.Konsole
import org.kobjects.konsole.demo.checkers.checkers
import org.kobjects.konsole.demo.poker.Poker
import org.kobjects.konsole.demo.poker.PokerJ
import org.kobjects.konsole.demo.rockpaperscissors.rockPaperScissors

class Demo(
    val name: String,
    val code: suspend (Konsole) -> Unit
) {
    companion object {
        val ALL = listOf(
            Demo("Banner", ::banner),
            Demo("Checkers",  ::checkers),
            Demo("Poker") { Poker(it).run() },
            Demo("Rock, Paper, Scissors", ::rockPaperScissors)

        )
    }

    suspend fun run(konsole: Konsole) {
        println("run $this")
        code(konsole)
    }
}

