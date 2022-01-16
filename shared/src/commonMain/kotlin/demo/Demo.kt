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

class DemoKonsolePair(
    val demo: Demo,
    val konsole: Konsole
    ) {
    suspend fun run() {
        demo.code(konsole)
    }
}


