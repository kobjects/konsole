package org.kobjects.konsole.demo.poker

import org.kobjects.konsole.Konsole
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random

fun random() = Random.Default.nextDouble()

fun random10() = (10 * random()).toInt()

class Poker(val konsole: Konsole) {
    val COMPUTER_OFFSET = 6
    val PLAYER_OFFSET = 1

    val cards = Array<Card>(17) { Card(0, 0) }
    var computerCash = 0
    var playerBet = 0
    var i = 0
    var computerBet = 0
    var pot = 0
    var playerCash = 0
    var t = 0.0
    var v = 0
    var x = 0
    var z = 0

    var watchSold = false
    var tieTackSold = false


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
    fun dealCard(z: Int, u: Int) {
        while (true) {
            cards[z] = Card(Random.Default.nextInt(12), Random.Default.nextInt(4))
            if (cards[z].suit > 3)    // Invalid suit
                continue;
            if (cards[z].value > 12) // Invalid number
                continue;
            if (z != 1) {
                var k = 1
                while (k <= z) {
                    if (cards[z] == cards[k]) {
                        break;
                    }
                    k++
                }
                if (k <= z - 1) // Repeated card
                    continue;
                if (z > 10) {
                    val n = cards[u];
                    cards[u] = cards[z];
                    cards[z] = n;
                }
            }
            return;
        }
    }

    // 1850
    fun describeCards(offset: Int): String {
        val sb = StringBuilder()
        for (z in offset .. (offset + 4)) {
            sb.append("${"¹²³⁴⁵"[(z-1)%5]} ");
            val card = cards[z]
            val value = card.value
            sb.append(when (value) {
                9 -> "J"
                10 -> "Q"
                11 -> "K"
                12 -> "A"
                else -> (value + 2).toString()
            })
            sb.append(when (card.suit) {
                0 -> "♣️"
                1 -> "♦️"
                2 -> "♥️"
                3 -> "♠️"
                else -> throw IllegalArgumentException()
            })
            if (z < offset + 4) {
                sb.append("│")
            }
        }
        return sb.toString()
    }


    // 2170
    fun evaluateHand(n: Int, i0: Int): Evaluation {
        val values = IntArray(cards.size)
        var hs = ""
        i = i0
        var u = 0
        for (z in n..(n + 4)) {
            values[z] = cards[z].value;
            if (z != n + 4) {
                if (cards[z].suit == cards[z + 1].suit) {
                    u++;
                }
            }
        }

        // Flush?
        if (u == 4) {
            x = 11111;
            hs = "a Flush in ";
            u = 15;
            return Evaluation(x, cards[n], hs, u, i)
        }

        var d = Card(0,0)
        x = 0;
        for (z in n..(n + 3)) {
            if (values[z] == values[z + 1]) {
                x += 11 * 10.0.pow(z - n).toInt()
                d = cards[z];
                if (u < 11) {
                    u = 11;
                    hs = "a pair of ";
                } else if (u == 11) {
                    if (values[z] == values[z - 1]) {
                        hs = "three ";
                        u = 13;
                    } else {
                        hs = "two pair, ";
                        u = 12;
                    }
                } else if (u == 12) {
                    u = 16;
                    hs = "Full House, ";
                } else if (values[z] == values[z - 1]) {
                    u = 17;
                    hs = "four ";
                } else {
                    u = 16;
                    hs = "Full House. ";
                }
            }
        }
        if (x == 0) {
            if (values[n] + 3 == values[n + 3]) {
                x = 1111;
                u = 10;
            }
            if (values[n + 1] + 3 == values[n + 4]) {
                if (u == 10) {
                    u = 14;
                    hs = "Straight";
                    x = 11111;
                    d = cards[n + 4];
                    return Evaluation(x = x, d = d, hs = hs, u = u, i = i)
                }
                u = 10;
                x = 11110;
            }
        }
        if (u < 10) {
            d = cards[n + 4];
            hs = "Schmaltz, ";
            u = 9;
            x = 11000;
            i = 6;
            return Evaluation(x = x, d = d, hs = hs, u = u, i = i);
        }
        if (u == 10) {
            if (i == 1)
                i = 6;
            return Evaluation(x = x, d = d, hs = hs, u = u, i = i);
        }
        if (u > 12 || d.value > 6)
            return Evaluation(x = x, d = d, hs = hs, u = u, i = i);

        i = 6;
        return Evaluation(x = x, d = d, hs = hs, u = u, i = i);
    }

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
        println()
        return konsole.read()
    }

    fun sortCards(n: Int) {
        for (z in n..(n + 3)) {
            for (k in (z + 1)..(n + 4)) {
                if (cards[z].value > cards[k].value) {
                    val x = cards[z];
                    cards[z] = cards[k];
                    cards[k] = x;
                }
            }
        }
    }


    // Main program
    suspend fun run() {
        println("POKER\nCreative Computing Morristown, New Jersey");
        println("""
            Welcome to the casino. We each have $200.
            I will open the betting before the draw; you open after.
            To fold bet 0; to check bet .5.""".trimIndent().replace('\n', ' '))
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
            for (z in 1..10) {
                dealCard(z, z)
            }
            sortCards(PLAYER_OFFSET)
            sortCards(COMPUTER_OFFSET)
            println("Your hand:\n${describeCards(1)}");
            i = 2

            var computerEvaluation = evaluateHand(COMPUTER_OFFSET, 2);
            first = true;
            if (i == 6) {
                if (random10() > 7) {
                    x = 11100;
                    i = 7;
                    z = 23;
                } else if (random10() > 7) {
                    x = 11110;
                    i = 7;
                    z = 23;
                } else if (random10() < 2) {
                    x = 11111;
                    i = 7;
                    z = 23;
                } else {
                    z = 1;
                    computerBet = 0;
                    println("I check.");
                    first = false;
                }
            } else {
                if (computerEvaluation.u < 13) {
                    if (random10() < 2) {
                        i = 7;
                        z = 23;
                    } else {
                        z = 0;
                        computerBet = 0;
                        println("I check.");
                        first = false;
                    }
                } else if (computerEvaluation.u > 16) {
                    z = 2;
                    if (random10() < 1)
                        z = 35;
                } else {
                    z = 35;
                }
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
            var cardCount = 10
            for (cn in cardNumbers) {
               dealCard(++cardCount, cn);
            }
            sortCards(PLAYER_OFFSET)
            println("Your new hand:\n${describeCards(1)}");
            for (u in 6..10) {
                if (floor(x / 10.0.pow(u - 6)) != 10 * floor(x / 10.0.pow(u - 5)))
                    break;
                dealCard(++cardCount, u);
            }
            sortCards(COMPUTER_OFFSET)
            val sb = StringBuilder("I am taking ${cardCount - 10 - cardNumbers.size} card");
            if (cardCount != 11 + cardNumbers.size) {
                sb.append("s");
            }
            println(sb.toString())

            v = i;
            i = 1;
            computerEvaluation = evaluateHand(COMPUTER_OFFSET, 1);
            if (v == 7) {
                z = 28;
            } else if (i == 6) {
                z = 1;
            } else {
                if (computerEvaluation.u < 13) {
                    z = 2;
                    if (random10() == 6)
                        z = 19;
                } else if (computerEvaluation.u < 16) {
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

            println("My Hand:\n${describeCards(COMPUTER_OFFSET)}");

            val playerEvaluation = evaluateHand(PLAYER_OFFSET, i);
            println("You have $playerEvaluation}")

            println("And I have $computerEvaluation}");

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

    class Card(val value: Int, val suit: Int): Comparable<Card> {
        override fun compareTo(other: Card): Int {
            return value.compareTo(other.value)
        }
    }

    class Evaluation(
        val x: Int,
        val d: Card,
        val hs: String,
        val u: Int,
        val i: Int
    ) : Comparable<Evaluation> {
        override fun compareTo(other: Evaluation): Int {
            val result = u.compareTo(other.u)
            return if (result != 0) result
                else d.value.compareTo(other.d.value)
        }

        override fun toString(): String {
            val sb = StringBuilder(hs);
            if (hs.startsWith("a Flush")) {
                sb.append(suitToString(d));
            } else {
                sb.append(valueToString(d));
                sb.append(if (hs.startsWith("Schmal")
                    || hs.startsWith("Straig"))
                    " high" else "s")
            }
            return sb.toString()
        }
    }
}

// 1950
fun valueToString(card: Poker.Card) = when(card.value) {
    9 -> "Jack"
    10 -> "Queen"
    11 -> "King"
    12 -> "Ace"
    else -> (card.value + 2).toString()
}

// 2070
fun suitToString(card: Poker.Card) = when(card.suit) {
    0 -> "Clubs"
    1 -> "Diamonds"
    2 -> "Hearts"
    3 -> "Spades"
    else -> throw IllegalArgumentException()
}
