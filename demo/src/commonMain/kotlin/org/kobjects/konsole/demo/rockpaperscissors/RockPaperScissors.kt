package org.kobjects.konsole.demo.rockpaperscissors

import kotlin.random.Random

enum class Choice {
   ROCK, PAPER, SCISSORS
}

fun processInput(input: String): Choice? =
    when (input.trim().lowercase()) {
        "rock", "r", "\uD83E\uDEA8" -> Choice.ROCK
        "paper", "p", "️\uD83D\uDDD2️",
        "\uD83E\uDDFB", "\uD83D\uDCF0", "\uD83D\uDCDD" -> Choice.PAPER
        "scissors", "s", "✂️" -> Choice.SCISSORS
        else -> null
    }

fun validateInput(input: String): String =
    if (processInput(input) == null) "'rock', 'paper' or 'scissors' expected."
    else ""

fun compare(userChoice: Choice, computerChoice: Choice) =
    when (userChoice) {
        Choice.ROCK -> when (computerChoice) {
            Choice.ROCK -> "Draw."
            Choice.PAPER -> "Paper wraps rock. I win."
            Choice.SCISSORS -> "Rock breaks scissors. You win."
        }
        Choice.PAPER -> when (computerChoice) {
            Choice.ROCK -> "Paper wraps rock. You win."
            Choice.PAPER -> "Draw."
            Choice.SCISSORS -> "Scissors cut paper. I win."
        }
        Choice.SCISSORS -> when (computerChoice) {
            Choice.ROCK -> "Rock breaks scissors. I win."
            Choice.PAPER -> "Scissors cur paper. You win."
            Choice.SCISSORS -> "Draw."
        }
    }

suspend fun readUserChoice(read: suspend (String?) -> String, write: (String) -> Unit): Choice {
    while (true) {
        val input = read("Rock, paper or scissors?")
        val errorMessage = validateInput(input)
        if (errorMessage.isEmpty()) {
            return processInput(input)!!
        }
        write(errorMessage)
    }
}

suspend fun rockPaperScissors(read: suspend (String?) -> String, write: (String) -> Unit) {
    while (true) {
        val userChoice = readUserChoice(read, write)
        val computerChoice = Choice.values()[Random.nextInt(0, 3)]

        write("I chose ${computerChoice.toString().lowercase()}")
        write(compare(userChoice, computerChoice))
    }
 }
