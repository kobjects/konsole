package org.kobjects.konsole.demo.poker

import kotlin.math.floor
import kotlin.math.min
import kotlin.random.Random

fun random() = Random.Default.nextDouble()

fun random10() = (10 * random()).toInt()

class Poker(
    val read: suspend (String?) -> String,
    val write: (String) -> Unit) {

    val allCards = mutableListOf<Card>()
    var computerCash = 0
    var playerCash = 0
    var pot = 0

    var watchSold = false
    var tieTackSold = false

    suspend fun queryCardNumbers(): List<Int> {
        write("Now we draw -- which cards do you want to replace?")
        while (true) {
            val s = read("Card numbers?")
            val cards = mutableListOf<Int>()
            var valid = true
            for (c in s) {
                if (c in '1'..'5') {
                    if (cards.size >= 3) {
                        write("No more than 3 cards.")
                        valid = false
                        break
                    }
                    cards.add(c.code - '0'.code)
                } else if (c > ' ' && c != ',' && c != '.' && c != ';') {
                    write("Invalid card number '$c'")
                    valid = false
                    break
                }
            }
            if (valid) {
                return cards.toList()
            }
            write("Please enter a list of card numbers, e.g. '1 2'. If you wish to replace no cards, don't fill anything")
        }
    }

    suspend fun yesNo(question: String): Boolean {
        while (true) {
            write(question)
            val text = read("y/n").lowercase()
            when (text) {
                "yes", "y" -> return true
                "no", "n" -> return false
            }
            write("Answer yes or no, please.")
        }
    }


    // 1740
    fun dealCard(): Card {
        var card: Card
        do {
            card = Card(2 + Random.Default.nextInt(12),
                Suit.values()[Random.Default.nextInt(4)])
        } while (allCards.contains(card))
        allCards.add(card)
        return card
    }


    suspend fun playerLowInMoney() {
        write("You can't bet with what you haven't got.")
        if (!watchSold) {
            if (yesNo("Would you like to sell your watch?")) {
                if (Random.Default.nextDouble() < 0.7) {
                    write("I'll give you $75 for it.\n")
                    playerCash += 75;
                } else {
                    write("That's a pretty crummy watch - I'll give you $25.\n")
                    playerCash += 25;
                }
                watchSold = true
                return
            }
        }
        if (!tieTackSold) {
            if (yesNo("Will you part with that diamond tie tack")) {
                if (Random.Default.nextDouble() < 0.6) {
                    write("You are now $100 richer.\n")
                    playerCash += 100;
                } else {
                    write("It's paste. $25.\n")
                    playerCash += 25;
                }
                tieTackSold = true
            }
        }
    }


    suspend fun computerLowInMoney() {
        if (watchSold) {
            if ( yesNo("Would you like to buy back your watch for $50")) {
                computerCash += 50
                watchSold = false
                return
            }
        }
        if (tieTackSold) {
            if (yesNo("Would you like to buy back your tie tack for $50")) {
                computerCash += 50;
                tieTackSold = false
                return
            }
        }
    }


    suspend fun askForAmount(minimum: Int): Int {
        while (true) {
            val question = if (minimum == 0)
                "Fold, check or rise to <amount>?"
             else
                "Fold, see or rise to <amount>?"

            val input = read(question).trim().lowercase()
            if (input == "f" || input == "fold") {
                return -1
            }
            if (minimum == 0) {
                if (input == "c" || input == "check") {
                    return 0
                }
            } else if (input == "s" || input == "see") {
                return minimum
            }
            try {
                val d = input.toDouble()
                if (d != floor(d) || d < 0) {
                    write("No small change, please.")
                } else if (d < minimum) {
                    write("If you can't see my bet, then fold.")
                } else {
                    return d.toInt()
                }
            } catch (e: Exception) {
                write("Amount not recognized.")
            }
        }
    }

    /**
     * Returns true if the game is over.
     */
    suspend fun bettingRound(beforeDraw: Boolean, computerRank: Rank): Boolean {
        var computerBet = 0
        var playerBet = 0

        if (beforeDraw) {
           val amount = calculateComputerBet(true, computerRank, 0)
            when (amount) {
                -1 -> {
                    playerCash += pot
                    pot = 0
                    write("I fold")
                    return true
                }
                0 -> {
                    write("I'll check.")
                }
                else -> {
                    computerBet = amount
                    write("I'll open with $$amount.")
                }
            }
        }

        while (true) {
            while (true) {
                val amount = askForAmount(computerBet)
                if (amount == -1) {
                    playerCash -= playerBet
                    computerCash += playerBet + pot
                    pot = 0
                    write("I win.")
                    return true
                }
                if (playerCash - amount < 0) {
                    playerLowInMoney()
                } else {
                    playerBet = amount
                    break
                }
            }

            if (playerBet == computerBet && (beforeDraw || computerBet != 0)) {
                break;
            }

            val amount = calculateComputerBet(beforeDraw, computerRank, playerBet)
            when (amount) {
                -1 -> {
                    computerCash -= computerBet
                    playerCash += computerBet + pot
                    pot = 0
                    write("I fold")
                    return true
                }
                playerBet -> {
                    write("I'll see you.")
                    computerBet = playerBet
                    break
                }
                else -> {
                    write("I raise to $amount")
                    computerBet = amount
                }
            }
        }
        playerCash -= playerBet;
        computerCash -= computerBet;
        pot += playerBet + computerBet;
        return false
    }

    fun calculateComputerBet(beforeDraw: Boolean, computerRank: Rank, playerBet: Int): Int {
        val computerScore = computerRank.ordinal + 1
        if (beforeDraw && playerBet == 0) {
            return if (computerScore + (random() * 1.5).toInt() == 0) 0
                else min(computerCash, 5 + ((random() * computerScore).toInt() / 5) * 5)
        }

        if (playerBet > computerCash / 2) {
            if (playerBet <= computerCash
                && computerScore + Random.Default.nextDouble() * 3 > 5) {
                return playerBet
            }
            return -1
        }
        return min(computerCash,  playerBet + (2 + (random() * computerScore).toInt() / 5) * 5)
    }


    // Main program
    suspend fun run() {
        write("POKER\nCreative Computing Morristown, New Jersey")
        write(
            """
            Welcome to the casino. We each have $200.
            I will open the betting before the draw; you open after.""".trimIndent()
        )
        write("Enough talk -- let's get down to business.")
        computerCash = 200
        playerCash = 200

        while (true) {
            playRound()

            write("Now I have $$computerCash and you have $$playerCash.")

            if (computerCash <= 5) {
                computerLowInMoney()
            }
            if (computerCash <= 5) {
                write("I'm busted. Congratulations!")
                break
            }
            if (!yesNo("Another round?")) {
                break;
            }
            if (playerCash <= 5) {
                playerLowInMoney()
            }
            if (playerCash <= 5) {
                write("Your wad is shot!")
                break;
            }
        }
    }

    suspend fun playRound() {
        write("The ante is $5, I will deal.")
        pot += 10;
        playerCash -= 5;
        computerCash -= 5;
        allCards.clear()

        val computerHand = Hand(List(5) { dealCard() })
        val playerHand = Hand(List(5) { dealCard() })

        playerHand.sort()
        write("Your hand:\n$playerHand")

        var computerEvaluation = computerHand.sortAndEvaluate()
        if (bettingRound(beforeDraw = true, computerRank = computerEvaluation.rank)) {
            return
        }

        val cardNumbers = queryCardNumbers()
        for (cn in cardNumbers) {
            playerHand.replaceCard(cn - 1, dealCard());
        }
        playerHand.sort()
        write("Your new hand:\n$playerHand")
        var count = 0
        for (u in 0..4) {
            if (!computerEvaluation.hold[u]) {
                computerHand.replaceCard(u, dealCard());
                if (++count == 3) {
                    break;
                }
            }
        }

        if (count == 1) {
            write("I am taking one card.")
        } else {
            write("I am taking $count cards.")
        }
        computerEvaluation = computerHand.sortAndEvaluate();
        if (bettingRound(false, computerEvaluation.rank)) {
            return
        }

        write("Now we compare hands.")
        write("My Hand:\n$computerHand")
        val playerEvaluation = playerHand.sortAndEvaluate();
        write("You have $playerEvaluation.")

        write("And I have $computerEvaluation.")
        when (computerEvaluation.compareTo(playerEvaluation)) {
            1 -> {
                write("I win.")
                computerCash += pot
                pot = 0
            }
            -1 -> {
                write("You win.")
                playerCash += pot
                pot = 0
            }
            else -> {
                write("The hand is Drawn; all $$pot remains in the pot.")
            };
        }
    }

    enum class Suit {
        DIAMONDS, CLUBS, HEARTS, SPADES
    }

    class Card(val value: Int, val suit: Suit) : Comparable<Card> {
        override fun compareTo(other: Card): Int = value.compareTo(other.value)

        override fun equals(other: Any?) =
            other is Card && other.value == value && other.suit == suit

        fun valueToString() = when (value) {
            11 -> "Jack"
            12 -> "Queen"
            13 -> "King"
            14 -> "Ace"
            else -> value.toString()
        }

        override fun toString(): String =
            (if (value == 10) "10" else valueToString().substring(0, 1)) + when (suit) {
                Suit.DIAMONDS -> "♦\ufe0f️"
                Suit.CLUBS -> "♣\ufe0f️"
                Suit.HEARTS -> "♥\ufe0f"
                Suit.SPADES -> "♠\ufe0f"
            }
    }


    class Hand(initialCards: List<Card>) {
        val cards = initialCards.toMutableList()

        fun replaceCard(index: Int, card: Card) {
            cards[index] = card
        }

        fun sort() = cards.sort()

        override fun toString(): String {
            val sb = StringBuilder()
            for (z in cards.indices) {
                if (!sb.isEmpty()) {
                    sb.append("│")
                }
                sb.append(cards[z])
            }
            return sb.toString()
        }

        fun sortAndEvaluate(): Evaluation {
            sort()
            var count = 1
            var maxCount = 1
            var pivot = listOf(cards[4])
            var hold = MutableList(5) { false }
            var straight = true
            var flush = true
            for (i in 1..4) {
                if (cards[i].suit != cards[i - 1].suit) {
                    flush = false
                }
                if (cards[i].value != cards[i - 1].value + 1) {
                    straight = false
                }
                if (cards[i].value == cards[i - 1].value) {
                    count++
                    if (count > maxCount
                            || (count == maxCount && cards[i].value > pivot[0].value)) {
                        maxCount = count
                        pivot = listOf(cards[i])
                        hold[i] = true
                        hold[i - 1] = true
                    }
                } else {
                    count = 1
                }
            }

            if (straight && flush) {
                return Evaluation(
                    rank = Rank.STRAIGHT_FLUSH,
                    pivot = listOf(cards[4]),
                    hold = List(5) { true })
            }
            var rank: Rank
            when (maxCount) {
                1 -> {
                    rank = Rank.SCHMALTZ
                    pivot = cards.reversed()
                }
                4 -> {
                    rank = Rank.FOUR
                    if (cards[0].value == pivot[0].value) {
                        pivot = listOf(pivot[0], cards[4])
                    } else {
                        pivot = listOf(pivot[0], cards[0])
                    }
                }
                else -> {
                    var secondPair = false
                    for (i in 1..4) {
                        if (cards[i].value != pivot[0].value
                               && cards[i].value == cards[i - 1].value
                        ) {
                            pivot = listOf(pivot[0], cards[i])
                            hold[i] = true
                            hold[i - 1] = true
                            secondPair = true
                        }
                        break;
                    }
                    if (maxCount == 3) {
                        rank = if (secondPair) Rank.FULL_HOUSE else Rank.THREE
                    } else {
                        rank = if (secondPair) Rank.TWO_PAIR else Rank.A_PAIR
                    }
                }
            }
            if (rank < Rank.FULL_HOUSE) {
                if (flush) {
                    rank = Rank.FLUSH
                    pivot = cards.reversed()
                    hold.fill(true)
                } else if (straight) {
                    rank = Rank.STRAIGHT
                    pivot = cards.reversed()
                    hold.fill(true)
                }
            }
            return Evaluation(
                rank = rank,
                pivot = pivot,
                hold = hold)
        }
    }

    enum class Rank {
        SCHMALTZ, A_PAIR, TWO_PAIR, THREE, STRAIGHT, FLUSH, FULL_HOUSE, FOUR, STRAIGHT_FLUSH
    }


    class Evaluation(
        val rank: Rank,
        val pivot: List<Card>,
        val hold: List<Boolean>) : Comparable<Evaluation> {
        override fun compareTo(other: Evaluation): Int {
            var result = rank.compareTo(other.rank)
            var i = 0
            while (result == 0 && i < pivot.size) {
                result = pivot[i].compareTo(other.pivot[i])
            }
            return result
        }

        override fun toString(): String {
            val high = pivot[0].valueToString()
            return when (rank) {
                Rank.SCHMALTZ -> "Schmaltz, $high"
                Rank.A_PAIR -> "A pair, $high high"
                Rank.TWO_PAIR -> "Two Pair, ${high}s and ${pivot[1].valueToString()}"
                Rank.THREE -> "Three ${high}s\""
                Rank.STRAIGHT -> "$high-high straight"
                Rank.FLUSH -> "$high-high flush"
                Rank.FULL_HOUSE ->  "Full House, ${high}s over ${pivot[1].valueToString()}s"
                Rank.FOUR -> "Four ${high}s."
                Rank.STRAIGHT_FLUSH -> "$high-high straight flush"
            }
        }
    }

}

