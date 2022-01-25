package org.kobjects.konsole.demo

import Hangman
import org.kobjects.konsole.demo.banner.banner
import org.kobjects.konsole.Konsole
import org.kobjects.konsole.demo.checkers.checkers
import org.kobjects.konsole.demo.poker.Poker
import org.kobjects.konsole.demo.rockpaperscissors.rockPaperScissors

class Demo(
    val number: Int,
    val name: String,
    val code: suspend (Konsole) -> Unit
) {
    companion object {
        val ALL = listOf(
            Demo(6,"Banner", ::banner),
            Demo(23,"Checkers",  ::checkers),
            Demo(44, "Hangman") { Hangman(it).run() },
            Demo(71, "Poker") { Poker(it).run() },
            Demo(74,"Rock, Paper, Scissors", ::rockPaperScissors)

        )
    }

    suspend fun run(konsole: Konsole) {
        println("run $this")
        code(konsole)
    }
}

