package demo

import demo.banner.banner
import org.kobjects.konsole.Konsole
import rockPaperScissors

class Demo(
    val name: String,
    val code: suspend (Konsole) -> Unit
) {
    companion object {
        val ALL = listOf(
            Demo("Banner", ::banner),
            Demo("Rock, Paper, Scissors", ::rockPaperScissors)
        )
    }

    suspend fun run(konsole: Konsole) {
        println("run $this")
        code(konsole)
    }
}

