package org.kobjects.konsole.demo

import org.kobjects.konsole.Konsole
import rockPaperScissors

class Demo(
    val name: String,
    val code: suspend (Konsole) -> Unit
) {
    companion object {
        val ALL = listOf(
            Demo("Rock, Paper, Scissors", ::rockPaperScissors)
        )
    }
}

