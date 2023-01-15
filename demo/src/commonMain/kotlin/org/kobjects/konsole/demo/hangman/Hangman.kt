package org.kobjects.konsole.demo.hangman

import kotlin.random.Random

/**
 * HANGMAN
 *
 * Converted from BASIC to Java by Aldrin Misquitta (@aldrinm)
 */

class Hangman(
    val read: suspend (String?) -> String,
    val write: (String) -> Unit
) {

    //50 word list
    private val WORDS = listOf(
        "GUM", "SIN", "FOR", "CRY", "LUG", "BYE", "FLY",
        "UGLY", "EACH", "FROM", "WORK", "TALK", "WITH", "SELF",
        "PIZZA", "THING", "FEIGN", "FIEND", "ELBOW", "FAULT", "DIRTY",
        "BUDGET", "SPIRIT", "QUAINT", "MAIDEN", "ESCORT", "PICKAX",
        "EXAMPLE", "TENSION", "QUININE", "KIDNEY", "REPLICA", "SLEEPER",
        "TRIANGLE", "KANGAROO", "MAHOGANY", "SERGEANT", "SEQUENCE",
        "MOUSTACHE", "DANGEROUS", "SCIENTIST", "DIFFERENT", "QUIESCENT",
        "MAGISTRATE", "ERRONEOUSLY", "LOUDSPEAKER", "PHYTOTOXIC",
        "MATRIMONIAL", "PARASYMPATHOMIMETIC", "THIGMOTROPISM"
    )

    suspend fun run() {
        write("Hangman\nCreative Computing Morristown, New Jersey")

        var remainingWords = WORDS.toMutableList()

        do {
            if (remainingWords.isEmpty()) {
                write("You did all the words!!")
                break
            }
            var wordIndex = Random.Default.nextInt(remainingWords.size)
            val youWon = playRound(remainingWords[wordIndex])
            remainingWords.removeAt(wordIndex)

            if (youWon) {
                write("Want another word?")
            } else {
               write("You missed that one. Do you want another word?")
            }
        } while (yesNo())
        write("It's been fun! Bye for now.")
    }

    fun createPicture(): List<StringBuilder> {
        val picture = List(12) { StringBuilder("╶╶╶╶╶╶╶╶╶╶╶╶") }
        for (i in picture.indices) {
            picture[i][0] = '║'
        }
        for (i in 1..6) {
            picture[0][i] = '═'
        }
        picture[0][0] = '╔'
        picture[0][6] = '╤'
        picture[1][6] = '│'
        return picture
    }

    private suspend fun playRound(word: String): Boolean {
        var misses = 0
        var totalGuesses = 0
        val lettersUsed = StringBuilder()
        val discovered = StringBuilder()
        for (c in word) {
            discovered.append('-');
        }
        val hangmanPicture = createPicture()

        while(misses < 10) {
            write("Misses: $lettersUsed\nDiscovered: $discovered")
            val guess = read("Your guess?").trim().uppercase()

            if (guess.length == 0) {
                write("Do you want to give up?")
                if (yesNo()) {
                    break
                }
            } else if (guess.length == 1) {
                val c = guess[0]
                if (c < 'A' || c > 'Z') {
                    write("That's not a valid letter for this game.")
                } else if (lettersUsed.contains(c) || discovered.contains(c)) {
                    write("You guessed that letter before!")
                } else {
                    totalGuesses++
                    if (word.indexOf(c) != -1) {
                        for (i in word.indices) {
                            if (word[i] == c) {
                                discovered.set(i, c)
                            }
                        }
                    } else {
                        write("Sorry, that letter isn't in the word.")
                        if (!lettersUsed.isEmpty()) {
                            lettersUsed.append(", ")
                        }
                        lettersUsed.append(c)
                        drawHangman(++misses, hangmanPicture)
                    }
                }
            } else {
                totalGuesses++
                if (guess == word) {
                    discovered.clear().append(word)
                } else {
                    write("Wrong.")
                    drawHangman(++misses, hangmanPicture)
                }
            }

            if (discovered.toString() == word) {
                write("You found the word '$word'! It took you $totalGuesses guesses!")
                return true
            }
        }

        write("Sorry, you lose. The word was $word")
        return false
    }

    private fun drawHangman(m: Int, hangmanPicture: List<StringBuilder>) {
        when (m) {
            1 -> {
                write("First, we draw a head.")
                hangmanPicture[2][4] = '╭'
                hangmanPicture[2][5] = '─'
                hangmanPicture[2][6] = '┴'
                hangmanPicture[2][7] = '─'
                hangmanPicture[2][8] = '╮'
                hangmanPicture[3][4] = '│'
                hangmanPicture[3][5] = '╹'
                hangmanPicture[3][7] = '╹'
                hangmanPicture[3][8] = '│'
                hangmanPicture[4][4] = '╰'
                hangmanPicture[4][5] = '─'
                hangmanPicture[4][6] = '╥'
                hangmanPicture[4][7] = '─'
                hangmanPicture[4][8] = '╯'
            }
            2 -> {
                write("Now we draw a body.")
                var i = 5
                while (i <= 8) {
                    hangmanPicture[i][6] = '║'
                    i++
                }
            }
            3 -> {
                write("Next we draw an arm.")
                var i = 3
                while (i <= 6) {
                    hangmanPicture[i][i - 1] = '╲'
                    i++
                }
            }
            4 -> {
                write("This time it's the other arm.")
                hangmanPicture[3][10] = '╱'
                hangmanPicture[4][9] = '╱'
                hangmanPicture[5][8] = '╱'
                hangmanPicture[6][7] = '╱'
            }
            5 -> {
                write("Now, let's draw the right leg.")
                hangmanPicture[9][5] = '╱'
                hangmanPicture[10][4] = '╱'
            }
            6 -> {
                write("this time we draw the left leg.")
                hangmanPicture[9][7] = '╲'
                hangmanPicture[10][8] = '╲'
            }
            7 -> {
                write("Now we put up a hand.")
                hangmanPicture[2][10] = '╲'
            }
            8 -> {
                write("Next the other hand.")
                hangmanPicture[2][2] = '╱'
            }
            9 -> {
                write("Now we draw one foot")
                hangmanPicture[11][9] = '╲'
                hangmanPicture[11][10] = '▁'
            }
            10 -> {
                write("Here's the other foot -- you're hung!!")
                hangmanPicture[11][2] = '▁'
                hangmanPicture[11][3] = '╱'
            }
        }
        val sb = StringBuilder()
        for (row in hangmanPicture) {
            sb.append(row)
            sb.append('\n')
        }
        sb.setLength(sb.length - 1)
        write(sb.toString())
    }

    suspend fun yesNo() = when (read(null).lowercase()) {
        "yes", "y" -> true
        else -> false

    }
}