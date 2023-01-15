package org.kobjects.konsole.demo

import org.kobjects.konsole.Konsole
import org.kobjects.konsole.demo.hangman.Hangman
import org.kobjects.konsole.demo.banner.banner
import org.kobjects.konsole.demo.checkers.checkers
import org.kobjects.konsole.demo.ktxml.ktXmlDemo
import org.kobjects.konsole.demo.poker.Poker
import org.kobjects.konsole.demo.rockpaperscissors.rockPaperScissors

class Demo(
    val number: Int,
    val name: String,
    val code: suspend (read: suspend (String?) -> String, write: (String) -> Unit) -> Unit
) {
    companion object {
        val ALL = listOf(
            Demo(6,"Banner", ::banner),
            Demo(23,"Checkers",  ::checkers),
            Demo(44, "Hangman") { read, write -> Hangman(read, write).run() },
            Demo(71, "Poker") { read, write -> Poker(read, write).run() },
            Demo(74,"Rock, Paper, Scissors", ::rockPaperScissors),
            Demo(0, "KtXml", ::ktXmlDemo)
        )
    }

    suspend fun run(konsole: Konsole) {
        run (konsole::read, konsole::write)
    }

    suspend fun run(read: suspend (String?) -> String, write: (String) -> Unit) {
        println("run $this")
        code(read, write)
    }
}

