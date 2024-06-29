package org.kobjects.konsole.demo

import org.kobjects.konsole.Konsole
import kotlin.random.Random

const val ERROR_MESSAGE = "'rock', 'paper' or 'scissors' expected."

fun main() {
    rockPaperScissors()
}

fun rockPaperScissors(konsole: Konsole = Konsole()) {
    konsole.readValidated("Rock, paper or scissors?", { if (parse(it) == null) ERROR_MESSAGE else "" }) {
        val computerChoice = Choice.values()[Random.nextInt(0, 3)]
        val userChoice = parse(it)
        if (userChoice == null) {
            konsole.println(ERROR_MESSAGE)
        } else {
            konsole.println("I chose ${computerChoice.name.lowercase()}.")
            konsole.println(compare(userChoice, computerChoice))
        }
        rockPaperScissors(konsole)
    }
}

fun parse(input: String): Choice? =
    when (input.trim().lowercase()) {
        "rock", "r", "\uD83E\uDEA8" -> Choice.ROCK
        "paper", "p", "️\uD83D\uDDD2️",
        "\uD83E\uDDFB", "\uD83D\uDCF0", "\uD83D\uDCDD" -> Choice.PAPER
        "scissors", "s", "✂️" -> Choice.SCISSORS
        else -> null
    }


fun compare(userChoice: Choice, computerChoice: Choice) =
    if (userChoice == computerChoice) "Draw."
    else if ((userChoice.ordinal + 1) % 3 == computerChoice.ordinal) userChoice.titleCase() + " " + userChoice.verb + " " + computerChoice.name.lowercase() + ". You win."
    else computerChoice.titleCase() + " " + computerChoice.verb + " " + userChoice.name.lowercase() + ". I win."


enum class Choice(val verb: String) {
    ROCK("breaks"), SCISSORS("cut"), PAPER("wraps");

    fun titleCase() = name[0] + name.substring(1).lowercase()
}