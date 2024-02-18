package org.kobjects.konsole.demo

import org.kobjects.konsole.Konsole
import org.kobjects.konsole.demo.hangman.Hangman
import org.kobjects.konsole.demo.banner.banner
import org.kobjects.konsole.demo.checkers.checkers
import org.kobjects.konsole.demo.ktxml.ktXmlDemo
import org.kobjects.konsole.demo.poker.Poker
import org.kobjects.konsole.demo.rockpaperscissors.rockPaperScissors
import org.kobjects.parserlib.examples.algebra.Cas
import org.kobjects.parserlib.examples.basic.Interpreter
import org.kobjects.parserlib.examples.calculator.Calculator
import org.kobjects.parserlib.examples.expressions.Context

class Demo(
    val name: String,
    val code: suspend (read: suspend (String?) -> String, write: (String) -> Unit) -> Unit
) {


    suspend fun run(konsole: Konsole) {
        run (konsole::read, konsole::write)
    }

    suspend fun run(read: suspend (String?) -> String, write: (String) -> Unit) {
        println("run $this")
        code(read, write)
    }

    companion object {
        val ALL = listOf(
            Demo( "BASIC") { read, write ->
                Interpreter(write, read).runShell()
            },
            Demo("Banner", ::banner),
            Demo("CAS") { read, write ->
                val cas = Cas()
                runShell(read, write, "Expression?") { cas.process(it) }
            },
            Demo("Checkers",  ::checkers),
            Demo("Expressions") {
                    read, write ->
                val context = Context()
                runShell(read, write, "Expression?") { context.eval(it) }
            },
            Demo( "Hangman") { read, write -> Hangman(read, write).run() },
            Demo("KtXml", ::ktXmlDemo),
            Demo("Poker") { read, write -> Poker(read, write).run() },
            /*
            Demo("Simple Calculator") {
                    read, write ->
                runShell(read, write, "Expression?") { Calculator.eval(it) }
            }
             */
            Demo("Rock, Paper, Scissors", ::rockPaperScissors),

            )

        suspend fun runShell(
            read: suspend (String?) -> String,
            write: (String) -> Unit,
            prompt: String,
            process: (String) -> Any
        ) {
            while (true) {
                val input = read(prompt)
                if (input.isBlank()) {
                    break
                }
                try {
                    write(process(input).toString())
                } catch (e: Exception) {
                    write(e.toString())
                }
            }
        }

    }
}

