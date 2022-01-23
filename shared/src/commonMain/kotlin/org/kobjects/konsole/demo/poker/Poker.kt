package org.kobjects.konsole.demo.poker

import org.kobjects.konsole.Konsole
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random

class Poker(val konsole: Konsole) {
    val cards = IntArray(17) { 0 }
    val ba = IntArray(17) { 0 }
    var b = 0
    var computerCash = 0
    var d = 0
    var g = 0
    var i = 0
    var k = 0
    var m = 0
    var n = 0
    var pot = 0
    var playerCash = 0
    var t = 0.0
    var u = 0
    var v = 0
    var x = 0
    var z = 0
    var hs = ""
    var js = ""

    var o = 0

    fun random() = Random.Default.nextDouble()

    fun random10() = (10 * random()).toInt()

    fun cardValue(card: Int) = card % 100

    fun cardSuit(card: Int) = card / 100

    fun imBusted() = println("I'm busted. Congratulations!")

    val printBuffer = StringBuilder()

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
    fun dealCard(z: Int) {
        while (true) {
            cards[z] = 100 * (4 * random()).toInt() + (12 * random()).toInt();
            if (cards[z] / 100 > 3)    // Invalid suit
                continue;
            if (cards[z] % 100 > 12) // Invalid number
                continue;
            if (z != 1) {
                k = 1
                while (k <= z) {
                    if (cards[z] == cards[k]) {
                        break;
                    }
                    k++
                }
                if (k <= z - 1) // Repeated card
                    continue;
                if (z > 10) {
                    n = cards[u];
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
            val value = cardValue(card)
            sb.append(when (value) {
                9 -> "J"
                10 -> "Q"
                11 -> "K"
                12 -> "A"
                else -> (value + 2).toString()
            })
            sb.append(when (cardSuit(card)) {
                0 -> "♣️"
                1 -> "♦️"
                2 -> "♥️"
                3 -> "♠️"
                else -> throw IllegalArgumentException()
            })
            if (z < offset + 4) {
                sb.append("│ ")
            }
        }
        return sb.toString()
    }

    // 1950
    fun valueToString(card: Int) = when(cardValue(card)) {
         9 -> "Jack"
        10 -> "Queen"
        11 -> "King"
        12 -> "Ace"
        else -> (cardValue(card) + 2).toString()
    }

    // 2070
    fun suitToString(card: Int) = when(card / 100) {
        0 -> "Clubs"
        1 -> "Diamonds"
        2 -> "Hearts"
        3 -> "Spades"
        else -> throw IllegalArgumentException()
    }

    // 2170
    fun evaluate_hand(n: Int) {
        u = 0;
        for (z in n..(n + 4)) {
            ba[z] = cardValue(cards[z]);
            if (z != n + 4) {
                if (cards[z] / 100 == cards[z + 1] / 100) {
                    u++;
                }
            }
        }
        if (u == 4) {
            x = 11111;
            d = cards[n];
            hs = "a Flush in ";
            u = 15;
            return;
        }
        for (z in n..(n + 3)) {
            for (k in (z + 1)..(n + 4)) {
                if (ba[z] > ba[k]) {
                    x = cards[z];
                    cards[z] = cards[k];
                    ba[z] = ba[k];
                    cards[k] = x;
                    ba[k] = cards[k] - 100 * (cards[k] / 100);
                }
            }
        }
        x = 0;
        for (z in n..(n + 3)) {
            if (ba[z] == ba[z + 1]) {
                x += 11 * 10.0.pow(z - n).toInt()
                d = cards[z];
                if (u < 11) {
                    u = 11;
                    hs = "a pair of ";
                } else if (u == 11) {
                    if (ba[z] == ba[z - 1]) {
                        hs = "three ";
                        u = 13;
                    } else {
                        hs = "two pair, ";
                        u = 12;
                    }
                } else if (u == 12) {
                    u = 16;
                    hs = "Full House, ";
                } else if (ba[z] == ba[z - 1]) {
                    u = 17;
                    hs = "four ";
                } else {
                    u = 16;
                    hs = "Full House. ";
                }
            }
        }
        if (x == 0) {
            if (ba[this.n] + 3 == ba[this.n + 3]) {
                x = 1111;
                u = 10;
            }
            if (ba[this.n + 1] + 3 == ba[this.n + 4]) {
                if (u == 10) {
                    u = 14;
                    hs = "Straight";
                    x = 11111;
                    d = cards[this.n + 4];
                    return;
                }
                u = 10;
                x = 11110;
            }
        }
        if (u < 10) {
            d = cards[this.n + 4];
            hs = "Schmaltz, ";
            u = 9;
            x = 11000;
            i = 6;
            return;
        }
        if (u == 10) {
            if (i == 1)
                i = 6;
            return;
        }
        if (u > 12)
            return;
        if (cardValue(d) > 6)
            return;
        i = 6;
    }

    fun println(s: String) {
       konsole.write(s)
    }


    suspend fun playerLowInMoney(): Boolean {
        println("You can't bet with what you haven't got.");
        var answer = false
        if (o % 2 != 0) {
            answer = yesNo("Would you like to sell your watch?")
            if (answer) {
                if (random10() < 7) {
                    println("I'll give you $75 for it.\n");
                    playerCash += 75;
                } else {
                    println("That's a pretty crummy watch - I'll give you $25.\n");
                    playerCash += 25;
                }
                o *= 2;
            }
        }
        if (o % 3 == 0 && !answer) {
            answer = yesNo("Will you part with that diamond tie tack")
            if (answer) {
                if (random10() < 6) {
                    println("You are now $100 richer.\n");
                    playerCash += 100;
                } else {
                    println("It's paste. $25.\n");
                    playerCash += 25;
                }
                o *= 3;
            }
        }
        if (!answer) {
            println("Your wad is shot. So long, sucker!");
            return true;
        }
        return false;
    }

    suspend fun computerLowInMoney(): Boolean {
        if (computerCash - g - v >= 0)
            return false;
        if (g == 0) {
            v = computerCash;
            return false;
        }
        if (computerCash - g < 0) {
            println("I'll see you.\n");
            k = g;
            playerCash = playerCash - g;
            computerCash = computerCash - k;
            pot = pot + g + k;
            return false;
        }
        var answer = false;
        if (o % 2 == 0) {
            answer = yesNo("Would you like to buy back your watch for $50")
            if (answer) {
                computerCash += 50;
                o /= 2;
            }
        }
        if (!answer && o % 3 == 0) {
            answer = yesNo("Would you like to buy back your tie tack for $50")
            if (answer) {
                computerCash += 50;
                o /= 3;
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
        val checkAllowed = k == 0 && g == 0
        while (true) {
            val sb = StringBuilder("What is your bet? Type 'fold' to fold");
            if (checkAllowed) {
                sb.append(" and 'check' to check.")
            } else {
                sb.append(".")
            }
            println(sb.toString());
            val input = input().trim().lowercase()
            when (input) {
                "c", "check" -> {
                    return 0.5
                }
                "f", "fold" -> {
                    return 0.0
                }
                else -> {
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
        }
    }

    suspend fun askForBetInner(): Int {
        t = askForAmount()
        if (t == 0.5) {
            return 1;
        }
        if (playerCash - g - t < 0) {
            if (playerLowInMoney())
                return 2;
            return 0;
        }
        if (t == 0.0) {
            i = 3;
        } else if (g + t < k) {
            println("If you can't see my bet, then fold.");
            return 0;
        } else {
            g += t.toInt();
            if (g != k) {
                var forced = false;
                if (z != 1) {
                    if (g <= 3 * z)
                        forced = true;
                } else {
                    if (g <= 5) {
                        if (z < 2) {
                            v = 5;
                            if (g <= 3 * z)
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
                    v = g - k + random10();
                    if (computerLowInMoney())
                        return 2;
                    println("I'll see you, and raise you $v");
                    k = g + v;
                    return 0;
                }
                println("I'll see you.");
                k = g;
            }
        }
        playerCash -= g;
        computerCash -= k;
        pot += g + k;
        return 1;
    }

    fun checkForWin(type: Int): Int {
        if (type == 0 && i == 3 || type == 1) {
            println("I win.");
            computerCash += pot;
        } else if (type == 0 && i == 4 || type == 2) {
            println("You win.");
            playerCash += pot;
        } else {
            return 0;
        }
        println("Now I have $$computerCash and you have $$playerCash.");
        return 1;
    }

    fun describeHand(hs: String, card: Int): String {
        val sb = StringBuilder(hs);
        if (hs.startsWith("a Flush")) {
            sb.append(suitToString(card));
        } else {
            sb.append(valueToString(card));
            sb.append(if (hs.startsWith("Schmal") || hs.startsWith("Straig"))
                " high" else "'s")
        }
        return sb.toString()
    }

    suspend fun input(): String {
        println()
        return konsole.read()
    }

    // Main program
    suspend fun run() {
        println("POKER\nCreative Computing Morristown, New Jersey");
        println("""
            Welcome to the casino. We each have $200.
            I will open the betting before the draw; you open after.
            To fold bet 0; to check bet .5.""".trimIndent().replace('\n', ' '))
        println("Enough talk -- let's get down to business.");
        o = 1;
        computerCash = 200;
        playerCash = 200;
        z = 0;
        var first = true
        var status = 0

        while (true) {
            pot = 0;
            if (computerCash <= 5) {
                imBusted();
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
                dealCard(z)
            }
            println("Your hand:\n${describeCards(1)}");
            n = 6;
            i = 2;
            evaluate_hand(6);
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
                    k = 0;
                    println("I check.");
                    first = false;
                }
            } else {
                if (u < 13) {
                    if (random10() < 2) {
                        i = 7;
                        z = 23;
                    } else {
                        z = 0;
                        k = 0;
                        println("I check.");
                        first = false;
                    }
                } else if (u > 16) {
                    z = 2;
                    if (random10() < 1)
                        z = 35;
                } else {
                    z = 35;
                }
            }
            if (first) {
                v = z + random10();
                g = 0;
                if (computerLowInMoney())
                    return;
                println("I'll open with $" + v);
                k = v;
            }
            g = 0;
            status = askForBet();
            if (status == 2)
                return;
            status = checkForWin(0);
            if (status == 1) {
                status = if (yesNo("Do you wish to continue?")) 1 else 2
            }
            if (status == 2)
                return;
            if (status == 1) {
                pot = 0;
                continue;
            }

            val cardNumbers = queryCardNumbers()
            z = 10
            for (cn in cardNumbers) {
               u = cn;
               dealCard(++z);
            }
            println("Your new hand:\n${describeCards(1)}");
            for (uu in 6..10) {
                u = uu
                if (floor(x / 10.0.pow(u - 6)) != 10 * floor(x / 10.0.pow(u - 5)))
                    break;
                dealCard(++z);
            }
            val sb = StringBuilder("I am taking ${z - 10 - cardNumbers.size} card");
            if (z != 11 + cardNumbers.size) {
                sb.append("s");
            }
            println(sb.toString())
            n = 6;
            v = i;
            i = 1;
            evaluate_hand(6);
            b = u;
            m = d;
            if (v == 7) {
                z = 28;
            } else if (i == 6) {
                z = 1;
            } else {
                if (u < 13) {
                    z = 2;
                    if (random10() == 6)
                        z = 19;
                } else if (u < 16) {
                    z = 19;
                    if (random10() == 8)
                        z = 11;
                } else {
                    z = 2;
                }
            }
            k = 0;
            g = 0;
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
                    k = v;
                    status = askForBet();
                    if (status == 2)
                        return;
                    status = checkForWin(0);
                    if (status == 1) {
                         status = if (yesNo("Do you wish to continue?")) 1 else 2
                    }
                    if (status == 2)
                        return;
                    if (status == 1) {
                        pot = 0;
                        continue;
                    }
                }
            } else {
                status = checkForWin(0);
                if (status == 1) {
                    status = if (yesNo("Do you wish to continue?")) 1 else 2
                }
                if (status == 2)
                    return;
                if (status == 1) {
                    pot = 0;
                    continue;
                }
            }
            println("Now we compare hands.");
            js = hs;

            println("My Hand:\n${describeCards(6)}");
            n = 1;
            evaluate_hand(1);
            println("You have ${describeHand(hs, d)}");
            hs = js;

            println("And I have ${describeHand(hs, m)}");
            status = 0;
            if (b > u) {
                status = 1;
            } else if (u > b) {
                status = 2;
            } else {
                if (!hs.startsWith("a Flus")) {
                    if (cardValue(m) < cardValue(d))
                        status = 2;
                    else if (cardValue(m) > cardValue(d))
                        status = 1;
                } else {
                    if (cardValue(m) > cardValue(d))
                        status = 1;
                    else if (cardValue(d) > cardValue(m))
                        status = 2;
                }
                if (status == 0) {
                    println("The hand is Drawn; all $" + pot + " remains in the pot.\n");
                    continue;
                }
            }
            status = checkForWin(status);
            if (status == 1) {
                status = if (yesNo("Do you wish to continue?")) 1 else 2
            }
            if (status == 2)
                return;
            if (status == 1) {
                pot = 0;
                continue;
            }
        }
    }
}
