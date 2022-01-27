package org.kobjects.konsole.demo.poker

import org.kobjects.konsole.Konsole
import kotlin.math.floor
import kotlin.random.Random

fun random() = Random.Default.nextDouble()

fun random10() = (10 * random()).toInt()

class Poker(val konsole: Konsole) {
    val COMPUTER_OFFSET = 5
    val PLAYER_OFFSET = 0

    val allCards = mutableListOf<Card>()
    var computerCash = 0
    var playerBet = 0
    var i = 0
    var computerBet = 0
    var pot = 0
    var playerCash = 0
    var t = 0.0
    var v = 0
    var z = 0

    var watchSold = false
    var tieTackSold = false

    var playerHand = Hand()
    var computerHand = Hand()

    fun printBusted() = println("I'm busted. Congratulations!")

    suspend fun queryCardNumbers(): List<Int> {
        println("Now we draw -- which cards do you want to replace?");
        while (true) {
            val s = konsole.read()
            val cards = mutableListOf<Int>()
            var valid = true
            for (c in s) {
                if (c in '1'..'5') {
                    if (cards.size >= 3) {
                        println("No more than 3 cards.")
                        valid = false
                        break
                    }
                    cards.add(c.code - '0'.code)
                } else if (c > ' ' && c != ',' && c != '.' && c != ';') {
                    println("Invalid card number '$c'")
                    valid = false
                    break
                }
            }
            if (valid) {
                println("Drawing $cards.")
                return cards.toList()
            }
            println("Please enter a list of card numbers, e.g. '1 2'. If you wish to replace no cards, don't fill anything")
        }
    }

    suspend fun yesNo(question: String): Boolean {
        while (true) {
            println(question)
            val text = konsole.read().lowercase()
            when (text) {
                "yes", "y" -> return true
                "no", "n" -> return false
            }
            println("Answer yes or no, please.")
        }
    }


    // 1740
    fun dealCard(): Card {
        var card: Card
        do {
            card = Card(Random.Default.nextInt(12), Random.Default.nextInt(4))
        } while (allCards.contains(card))
        allCards.add(card)
        return card
    }

    // 1850


    // 2170

    fun println(s: String) {
        konsole.write(s)
    }


    suspend fun playerLowInMoney(): Boolean {
        println("You can't bet with what you haven't got.");
        var answer = false
        if (!watchSold) {
            answer = yesNo("Would you like to sell your watch?")
            if (answer) {
                if (random10() < 7) {
                    println("I'll give you $75 for it.\n");
                    playerCash += 75;
                } else {
                    println("That's a pretty crummy watch - I'll give you $25.\n");
                    playerCash += 25;
                }
                watchSold = true
            }
        }
        if (!tieTackSold && !answer) {
            answer = yesNo("Will you part with that diamond tie tack")
            if (answer) {
                if (random10() < 6) {
                    println("You are now $100 richer.\n");
                    playerCash += 100;
                } else {
                    println("It's paste. $25.\n");
                    playerCash += 25;
                }
                tieTackSold = false
            }
        }
        if (!answer) {
            println("Your wad is shot. So long, sucker!");
            return true;
        }
        return false;
    }

    suspend fun computerLowInMoney(): Boolean {
        if (computerCash - playerBet - v >= 0)
            return false;
        if (playerBet == 0) {
            v = computerCash;
            return false;
        }
        if (computerCash - playerBet < 0) {
            println("I'll see you.\n");
            computerBet = playerBet;
            playerCash = playerCash - playerBet;
            computerCash = computerCash - computerBet;
            pot = pot + playerBet + computerBet;
            return false;
        }
        var answer = false;
        if (watchSold) {
            answer = yesNo("Would you like to buy back your watch for $50")
            if (answer) {
                computerCash += 50
                watchSold = false
            }
        }
        if (!answer && tieTackSold) {
            answer = yesNo("Would you like to buy back your tie tack for $50")
            if (answer) {
                computerCash += 50;
                tieTackSold = false
            }
        }
        if (!answer) {
            println("I'm busted. Congratulations!");
            return true;
        }
        return false;
    }

    suspend fun askForBet(): Int {
        while (true) {
            val status = askForBetInner()
            if (status != 0) {
                return status;
            }
        }
    }

    suspend fun askForAmount(): Double {
        val checkAllowed = computerBet == 0 && playerBet == 0
        while (true) {
            val sb = StringBuilder("What is your bet? Type 'fold' to fold");
            if (checkAllowed) {
                sb.append(" and 'check' to check.")
            } else {
                sb.append(".")
            }
            println(sb.toString());
            val input = input().trim().lowercase()
            if (checkAllowed && (input == "c" || input == "check")) {
                return 0.5
            }
            if (input == "f" || input == "fold") {
                return 0.0
            }
            try {
                val d = input.toDouble()
                if (d == floor(d) && d > 0) {
                    return d
                }
                println("No small change, please.");
            } catch (e: Exception) {
                println("Amount not recognized.")
            }
        }
    }

    suspend fun askForBetInner(): Int {
        t = askForAmount()
        if (t == 0.5) {
            return 1;
        }
        if (playerCash - playerBet - t < 0) {
            if (playerLowInMoney())
                return 2;
            return 0;
        }
        if (t == 0.0) {
            i = 3;
        } else if (playerBet + t < computerBet) {
            println("If you can't see my bet, then fold.");
            return 0;
        } else {
            playerBet += t.toInt();
            if (playerBet != computerBet) {
                var forced = false;
                if (z != 1) {
                    if (playerBet <= 3 * z)
                        forced = true;
                } else {
                    if (playerBet <= 5) {
                        if (z < 2) {
                            v = 5;
                            if (playerBet <= 3 * z)
                                forced = true;
                        }
                    } else {
                        if (z == 1 || t > 25) {
                            i = 4;
                            println("I fold.");
                            return 1;
                        }
                    }
                }
                if (forced || z == 2) {
                    v = playerBet - computerBet + random10();
                    if (computerLowInMoney())
                        return 2;
                    println("I'll see you, and raise you $v");
                    computerBet = playerBet + v;
                    return 0;
                }
                println("I'll see you.");
                computerBet = playerBet;
            }
        }
        playerCash -= playerBet;
        computerCash -= computerBet;
        pot += playerBet + computerBet;
        return 1;
    }

    suspend fun checkForWin(type: Int): Int {
        if (type == 1) {
            println("I win.");
            computerCash += pot;
            return if (yesNo("Do you wish to continue?")) 1 else 2
        } else if (type == 2) {
            println("You win.");
            playerCash += pot;
        } else {
            return 0;
        }
        println("Now I have $$computerCash and you have $$playerCash.");
        return 1;
    }


    suspend fun input(): String {
        return konsole.read()
    }

    // Main program
    suspend fun run() {
        println("POKER\nCreative Computing Morristown, New Jersey");
        println(
            """
            Welcome to the casino. We each have $200.
            I will open the betting before the draw; you open after.
            To fold bet 0; to check bet .5.""".trimIndent().replace('\n', ' ')
        )
        println("Enough talk -- let's get down to business.");

        computerCash = 200;
        playerCash = 200;
        z = 0;
        var first = true
        var status = 0

        while (true) {
            pot = 0;
            if (computerCash <= 5) {
                printBusted();
                return;
            }
            println("The ante is $5, I will deal.");
            if (playerCash <= 5) {
                if (playerLowInMoney())
                    return;
            }
            pot += 10;
            playerCash -= 5;
            computerCash -= 5;
            allCards.clear()
            computerHand = Hand()
            playerHand = Hand()

            playerHand.sort()
            println("Your hand:\n$playerHand");

            var computerEvaluation = computerHand.sortAndEvaluate();

                if (computerEvaluation.score < 13) {
                    if (random10() < 2) {
                        z = 23;
                    } else {
                        z = 0;
                        computerBet = 0;
                        println("I check.");
                        first = false;
                    }
                } else if (computerEvaluation.score > 16) {
                    z = 2;
                    if (random10() < 1)
                        z = 35;
                } else {
                    z = 35;
                }

            if (first) {
                v = z + random10();
                playerBet = 0;
                if (computerLowInMoney())
                    return;
                println("I'll open with $" + v);
                computerBet = v;
            }
            playerBet = 0;
            status = askForBet();
            if (status == 2)
                return;
            status = checkForWin(i - 2);
            if (status == 2)
                return;
            if (status == 1) {
                pot = 0;
                continue;
            }

            val cardNumbers = queryCardNumbers()
            for (cn in cardNumbers) {
                playerHand.replaceCard(cn - 1);
            }
            playerHand.sort()
            println("Your new hand:\n$playerHand");
            var count = 0
            for (u in 0..4) {
                if (!computerEvaluation.hold[u]) {
                    computerHand.replaceCard(u);
                    if (++count == 3) {
                        break;
                    }
                }
            }

            val sb = StringBuilder("I am taking $count card");
            if (count != 1) {
                sb.append("s");
            }
            println(sb.toString())

            v = i;
            i = 1;
            computerEvaluation = computerHand.sortAndEvaluate();
            if (v == 7) {
                z = 28;
            } else if (i == 6) {
                z = 1;
            } else {
                if (computerEvaluation.score < 13) {
                    z = 2;
                    if (random10() == 6)
                        z = 19;
                } else if (computerEvaluation.score < 16) {
                    z = 19;
                    if (random10() == 8)
                        z = 11;
                } else {
                    z = 2;
                }
            }
            computerBet = 0;
            playerBet = 0;
            status = askForBet();
            if (status == 2)
                return;
            if (t == 0.5) {
                if (v != 7 && i == 6) {
                    println("I'll check");
                } else {
                    v = z + random10();
                    if (computerLowInMoney())
                        return;
                    println("I'll bet $$v");
                    computerBet = v;
                    status = askForBet();
                    if (status == 2)
                        return;
                    status = checkForWin(i - 2);
                    if (status == 2)
                        return;
                    if (status == 1) {
                        pot = 0;
                        continue;
                    }
                }
            } else {
                status = checkForWin(i - 2);
                if (status == 2)
                    return;
                if (status == 1) {
                    pot = 0;
                    continue;
                }
            }
            println("Now we compare hands.");

            println("My Hand:\n$computerHand");

            val playerEvaluation = playerHand.sortAndEvaluate();
            println("You have $playerEvaluation.")

            println("And I have $computerEvaluation.");

            val result = computerEvaluation.compareTo(playerEvaluation)

            status = if (result == -1) 2 else result;
            if (status == 0) {
                println("The hand is Drawn; all $$pot remains in the pot.");
                continue;
            }
            status = checkForWin(status);
            if (status == 2)
                return;
            if (status == 1) {
                pot = 0;
                continue;
            }
        }
    }

    class Card(val value: Int, val suit: Int) : Comparable<Card> {
        override fun compareTo(other: Card): Int = value.compareTo(other.value)

        override fun equals(other: Any?) =
            other is Card && other.value == value && other.suit == suit

        fun valueToString() = when (value) {
            9 -> "Jack"
            10 -> "Queen"
            11 -> "King"
            12 -> "Ace"
            else -> (value + 2).toString()
        }

        fun suitToString() = when (suit) {
            0 -> "Clubs"
            1 -> "Diamonds"
            2 -> "Hearts"
            3 -> "Spades"
            else -> throw IllegalArgumentException()
        }

        override fun toString(): String =
            when (value) {
                9 -> "J"
                10 -> "Q"
                11 -> "K"
                12 -> "A"
                else -> (value + 2).toString()
            } + when (suit) {
                0 -> "♣️"
                1 -> "♦️"
                2 -> "♥️"
                3 -> "♠️"
                else -> throw IllegalArgumentException()
            }
    }


    inner class Hand() {
        var cards = MutableList<Card>(5) { dealCard() }

        fun replaceCard(index: Int) {
            cards[index] = dealCard()
        }

        fun sort() = cards.sort()

        override fun toString(): String {
            val sb = StringBuilder()
            for (z in cards.indices) {
                if (!sb.isEmpty()) {
                    sb.append("│")
                }
                sb.append("¹²³⁴⁵"[z])
                sb.append("ᐟ")
                sb.append(cards[z])
            }
            return sb.toString()
        }

        fun sortAndEvaluate(): Evaluation {
            sort()
            var count = 1
            var maxCount = 1
            var pivot = listOf(cards[4])
            var keep = MutableList(5) { false }
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
                    if (++count > maxCount) {
                        maxCount = count
                        pivot = listOf(cards[i])
                        keep[i] = true
                        keep[i - 1] = true
                    }
                } else {
                    count = 1
                }
            }

            var valueName = pivot[0].valueToString()
            var score: Int
            var description: String

            when (maxCount) {
                1 -> {
                    description = "Schmaltz, ${valueName}"
                    score = cards[4].value
                    pivot = cards.subList(0, 3).reversed()
                }
                4 -> {
                    description = "Four ${valueName}s."
                    score = 17
                }
                else -> {
                    var secondPair = false
                    for (i in 1..4) {
                        if (cards[i].value != pivot[0].value && cards[i].value == cards[i - 1].value) {
                            pivot = listOf(pivot[0], cards[i])
                            secondPair = true
                        }
                        break;
                    }
                    if (secondPair) {
                        if (maxCount == 2) {
                            score = 12
                            description = "Two Pair, ${valueName}s and ${pivot[1].valueToString()}"
                        } else {
                            score = 16
                            description = "Full House, ${valueName}s over ${pivot[1].valueToString()}s"
                        }
                    } else {
                        if (maxCount == 2) {
                            score = 11
                            description = "A pair of ${valueName}s"
                        } else {
                            score = 13
                            description = "Three ${valueName}s\""
                        }
                    }
                }
            }

            if (straight && flush) {
                description = "${cards[4].valueToString()}-high straight flush"
                score = 18
                pivot = listOf(cards[4])
                keep.fill(true)
            } else if (score < 16) {
                if (flush) {
                    description = "${cards[4].valueToString()}-high flush"
                    score = 15
                    pivot = cards.reversed()
                    keep.fill(true)
                } else if (straight) {
                    description = "${cards[4].valueToString()}-high straight"
                    score = 14
                    keep.fill(true)
                }
            }

            return Evaluation(hold = keep, pivot = pivot, description = description, score = score)
        }


    }


    class Evaluation(
        val hold: List<Boolean>,
        val pivot: List<Card>,
        val description: String,
        val score: Int
    ) : Comparable<Evaluation> {
        override fun compareTo(other: Evaluation): Int {
            var result = score.compareTo(other.score)
            var i = 0
            while (result == 0 && i < pivot.size) {
                result = pivot[i].compareTo(other.pivot[i])
            }
            return result
        }

        override fun toString(): String = description
    }

}

