import org.kobjects.konsole.Konsole
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


fun rockPaperScissors(konsole: Konsole) {
    konsole.input(
       "Rock, paper or scissors?",
        validation = {validateInput(it)}
    ) {
            val userChoice = processInput(it)!!
            val computerChoice = Choice.values()[Random.nextInt(0, 3)]

            konsole.print("I chose ${computerChoice.toString().lowercase()}")
            konsole.print(compare(userChoice, computerChoice))

            rockPaperScissors(konsole)
    }
}
