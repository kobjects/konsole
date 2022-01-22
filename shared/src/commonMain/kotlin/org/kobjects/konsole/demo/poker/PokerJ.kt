package org.kobjects.konsole.demo.poker

import org.kobjects.konsole.Konsole
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random


/**
 * Port of CREATIVE COMPUTING Poker written in Commodore 64 Basic to plain Java
 *
 * Original source scanned from magazine: https://www.atariarchives.org/basicgames/showpage.php?page=129
 *
 * I based my port on the OCR'ed source code here: https://github.com/coding-horror/basic-computer-games/blob/main/71_Poker/poker.bas
 *
 * Why? Because I remember typing this into my C64 when I was a tiny little developer and having great fun playing it!
 *
 * Goal: Keep the algorithms and UX more or less as-is; Improve the control flow a bit (no goto in Java!) and rename some stuff to be easier to follow.
 *
 * Result: There are probably bugs, please let me know.
 *
 * Ported to kotlin from https://github.com/coding-horror/basic-computer-games/blob/main/71_Poker/java/Poker.java
 */
class PokerJ(val konsole: Konsole) {
    var cards = FloatArray(50) // Index 1-5 = Human hand, index 6-10 = Computer hand
    var B = FloatArray(15)
    var playerValuables = 1f
    var computerMoney = 200f
    var humanMoney = 200f
    var pot = 0f
    var jstr = ""
    var computerHandValue = 0f
    var k = 0
    var g = 0f
    var t = 0f
    var m = 0
    var d = 0
    var u = 0
    var n = 1f
    var i = 0f
    var x = 0f
    var z = 0
    var handDescription = ""
    var v = 0f
    var buf = StringBuilder()

    fun print(s: String) {
        buf.append(s)
    }

    fun println(s: String = "") {
        buf.append(s)
        if (buf.length > 0) {
            konsole.write(buf.toString())
            buf.clear()
        }
    }

    suspend fun run() {
        printWelcome()
        playRound()
        startAgain()
    }

    fun printWelcome() {
        tab(33)
        println("POKER")
        tab(15)
        print("CREATIVE COMPUTING  MORRISTOWN, NEW JERSEY")
        println()
        println()
        println()
        println("WELCOME TO THE CASINO.  WE EACH HAVE $200.")
        println("I WILL OPEN THE BETTING BEFORE THE DRAW; YOU OPEN AFTER.")
        println("TO FOLD BET 0; TO CHECK BET .5.")
        println("ENOUGH TALK -- LET'S GET DOWN TO BUSINESS.")
        println()
    }

    fun tab(number: Int) {
        print("\t".repeat(number))
    }

    fun random0to10(): Int {
        return Random.Default.nextInt(10)
    }

    fun removeHundreds(x: Long): Int {
        return _int(x - 100f * _int(x / 100f))
    }

    suspend fun startAgain() {
        pot = 0f
        playRound()
    }

    suspend fun playRound() {
        if (computerMoney <= 5) {
            computerBroke()
        }
        println("THE ANTE IS $5.  I WILL DEAL:")
        println()
        if (humanMoney <= 5) {
            playerBroke()
        }
        pot = pot + 10
        humanMoney = humanMoney - 5
        computerMoney = computerMoney - 5
        for (Z in 1..9) {
            generateCards(Z)
        }
        println("YOUR HAND:")
        n = 1f
        showHand()
        n = 6f
        i = 2f
        describeHand()
        println()
        if (i != 6f) {
            if (u >= 13) {
                if (u <= 16) {
                    z = 35
                } else {
                    z = 2
                    if (random0to10() < 1) {
                        z = 35
                    }
                }
                computerOpens()
                playerMoves()
            } else if (random0to10() >= 2) {
                computerChecks()
            } else {
                i = 7f
                z = 23
                computerOpens()
                playerMoves()
            }
        } else if (random0to10() <= 7) {
            if (random0to10() <= 7) {
                if (random0to10() >= 1) {
                    z = 1
                    k = 0
                    print("I CHECK. ")
                    playerMoves()
                } else {
                    x = 11111f
                    i = 7f
                    z = 23
                    computerOpens()
                    playerMoves()
                }
            } else {
                x = 11110f
                i = 7f
                z = 23
                computerOpens()
                playerMoves()
            }
        } else {
            x = 11100f
            i = 7f
            z = 23
            computerOpens()
            playerMoves()
        }
    }

    suspend fun playerMoves() {
        playersTurn()
        checkWinnerAfterFirstBet()
        println()
        println("NOW WE DRAW -- HOW MANY CARDS DO YOU WANT")
        inputPlayerDrawCards()
    }

    suspend fun computerOpens() {
        v = (z + random0to10()).toFloat()
        computerMoves()
        print("I'LL OPEN WITH $$v")
        k = _int(v)
    }

    suspend fun computerMoves() {
        if (computerMoney - g - v >= 0) {
        } else if (g != 0f) {
            if (computerMoney - g >= 0) {
                computerSees()
            } else {
                computerBroke()
            }
        } else {
            v = computerMoney
        }
    }

    suspend fun inputPlayerDrawCards() {
        t = readString().toInt().toFloat()
        if (t == 0f) {
            computerDrawing()
        } else {
            z = 10
            if (t < 4) {
                playerDrawsCards()
            } else {
                println("YOU CAN'T DRAW MORE THAN THREE CARDS.")
                inputPlayerDrawCards()
            }
        }
    }

    // line # 980
    suspend fun computerDrawing() {
        z = _int(10 + t)
        u = 6
        while (u <= 10) {
            if (_int(
                    (x /
                            10.0.pow(
                                (u - 6f).toDouble()
                            )).toFloat()
                ) == 10 * _int((x / 10.0.pow((u - 5).toDouble())).toFloat())
            ) {
                drawNextCard()
            }
            u++
        }
        print("I AM TAKING " + _int(z - 10 - t) + " CARD")
        if (z.toFloat() == 11 + t) {
            println()
        } else {
            println("S")
        }
        n = 6f
        v = i
        i = 1f
        describeHand()
        startPlayerBettingAndReaction()
    }

    fun drawNextCard() {
        z = z + 1
        drawCard()
    }

    fun drawCard() {
        cards[z] = (100 * Random.Default.nextInt(4) + Random.Default.nextInt(100)).toFloat()
        if (_int(cards[z] / 100) > 3) {
            drawCard()
        } else if (cards[z] - 100 * _int(cards[z] / 100) > 12) {
            drawCard()
        } else if (z == 1) {
        } else {
            k = 1
            while (k <= z - 1) {
                if (cards[z] == cards[k]) {
                    drawCard()
                }
                k++
            }
            if (z <= 10) {
            } else {
                n = cards[u]
                cards[u] = cards[z]
                cards[z] = n
            }
        }
    }

    suspend fun playerDrawsCards() {
        println("WHAT ARE THEIR NUMBERS:")
        var Q = 1
        while (Q <= t) {
            u = readString().toInt()
            drawNextCard()
            Q++
        }
        println("YOUR NEW HAND:")
        n = 1f
        showHand()
        computerDrawing()
    }

    suspend fun startPlayerBettingAndReaction() {
        computerHandValue = u.toFloat()
        m = d
        if (v != 7f) {
            if (i != 6f) {
                if (u >= 13) {
                    if (u >= 16) {
                        z = 2
                        playerBetsAndComputerReacts()
                    } else {
                        z = 19
                        if (random0to10() == 8) {
                            z = 11
                        }
                        playerBetsAndComputerReacts()
                    }
                } else {
                    z = 2
                    if (random0to10() == 6) {
                        z = 19
                    }
                    playerBetsAndComputerReacts()
                }
            } else {
                z = 1
                playerBetsAndComputerReacts()
            }
        } else {
            z = 28
            playerBetsAndComputerReacts()
        }
    }

    suspend fun playerBetsAndComputerReacts() {
        k = 0
        playersTurn()
        if (t.toDouble() != .5) {
            checkWinnerAfterFirstBet()
            compareHands()
        } else if (v == 7f || i != 6f) {
            computerOpens()
            promptAndInputPlayerBet()
            checkWinnerAfterFirstBet()
            compareHands()
        } else {
            println("I'LL CHECK")
            compareHands()
        }
    }

    suspend fun compareHands() {
        println("NOW WE COMPARE HANDS:")
        jstr = handDescription
        println("MY HAND:")
        n = 6f
        showHand()
        n = 1f
        describeHand()
        print("YOU HAVE ")
        k = d
        printHandDescriptionResult()
        handDescription = jstr
        k = m
        print(" AND I HAVE ")
        printHandDescriptionResult()
        print(". ")
        if (computerHandValue > u) {
            computerWins()
        } else if (u > computerHandValue) {
            humanWins()
        } else if (handDescription.contains("A FLUS")) {
            someoneWinsWithFlush()
        } else if (removeHundreds(m.toLong()) < removeHundreds(d.toLong())) {
            humanWins()
        } else if (removeHundreds(m.toLong()) > removeHundreds(d.toLong())) {
            computerWins()
        } else {
            handIsDrawn()
        }
    }

    fun printHandDescriptionResult() {
        print(handDescription)
        if (!handDescription.contains("A FLUS")) {
            k = removeHundreds(k.toLong())
            print(cardValue(k))
            if (handDescription.contains("SCHMAL")) {
                print(" HIGH")
            } else if (!handDescription.contains("STRAIG")) {
                print("'S")
            } else {
                print(" HIGH")
            }
        } else {
            k = k / 100
            println(" " + cardColor(k))
        }
    }

    suspend fun handIsDrawn() {
        print("THE HAND IS DRAWN.")
        print("ALL $$pot REMAINS IN THE POT.")
        playRound()
    }

    suspend fun someoneWinsWithFlush() {
        if (removeHundreds(m.toLong()) > removeHundreds(d.toLong())) {
            computerWins()
        } else if (removeHundreds(d.toLong()) > removeHundreds(m.toLong())) {
            humanWins()
        } else {
            handIsDrawn()
        }
    }

    suspend fun checkWinnerAfterFirstBet() {
        if (i != 3f) {
            if (i != 4f) {
            } else {
                humanWins()
            }
        } else {
            println()
            computerWins()
        }
    }

    suspend fun computerWins() {
        print(". I WIN. ")
        computerMoney = computerMoney + pot
        potStatusAndNextRoundPrompt()
    }

    suspend fun potStatusAndNextRoundPrompt() {
        println("NOW I HAVE $$computerMoney AND YOU HAVE $$humanMoney")
        startAgain()
    }

    private suspend fun yesFromPrompt(): Boolean {
        val h = readString().lowercase()
        if (h != null) {
            if (h.matches(Regex("y|yes|yep|affirmative|yay"))) {
                return true
            } else if (h.matches(Regex("n|no|nope|fuck off|nay"))) {
                return false
            }
        }
        println("ANSWER YES OR NO, PLEASE.")
        return yesFromPrompt()
    }

    suspend fun computerChecks() {
        z = 0
        k = 0
        print("I CHECK. ")
        playerMoves()
    }

    suspend fun humanWins() {
        println("YOU WIN.")
        humanMoney = humanMoney + pot
        potStatusAndNextRoundPrompt()
    }

    // line # 1740
    fun generateCards(Z: Int) {
        cards[Z] = (100 * Random.Default.nextInt(4) + Random.Default.nextInt(100)).toFloat()
        if (_int(cards[Z] / 100) > 3) {
            generateCards(Z)
            return
        }
        if (cards[Z] - 100 * _int(cards[Z] / 100) > 12) {
            generateCards(Z)
            return
        }
        if (Z == 1) {
            return
        }
        for (K in 1..Z - 1) { // TO Z-1
            if (cards[Z] == cards[K]) {
                generateCards(Z)
                return
            }
        }
        if (Z <= 10) {
            return
        }
        val N = cards[u]
        cards[u] = cards[Z]
        cards[Z] = N
    }

    // line # 1850
    fun showHand() {
        var cardNumber = _int(n)
        while (cardNumber <= n + 4) {
            print("$cardNumber) ")
            printCardValueAtIndex(cardNumber)
            printCardColorAtIndex(cardNumber)
            if (cardNumber < n + 4) {
                print("│ ")
            }
            cardNumber++
        }
        println()
    }

    // line # 1950
    fun printCardValueAtIndex(Z: Int) {
        k = removeHundreds(_int(cards[Z]).toLong())
        print(
            when (k) {
                9 -> "J"
                10 -> "Q"
                11 -> "K"
                12 -> "A"
                else -> (k + 2).toString()
            }
        )
    }

    fun cardValue(k: Int) = when (k) {
        9 -> "JACK"
        10 -> "QUEEN"
        11 -> "KING"
        12 -> "ACE"
        else -> (k + 2).toString()
    }

    // line # 2070
    fun printCardColorAtIndex(Z: Int) {
        k = _int(cards[Z] / 100)
        print(
            when (k) {
                0 -> "♣️"
                1 -> "♦️"
                2 -> "♥️"
                3 -> "♠️"
                else -> throw IllegalArgumentException()
            }
        )
    }

    fun cardColor(k: Int) = when (k) {
        0 -> "CLUBS"
        1 -> "DIAMONDS"
        2 -> "HEARTS"
        3 -> "SPADES"
        else -> throw IllegalArgumentException()
    }

    // line # 2170
    fun describeHand() {
        u = 0
        z = _int(n)
        while (z <= n + 4) {
            B[z] = removeHundreds(_int(cards[z]).toLong()).toFloat()
            if (z.toFloat() == n + 4) {
                z++
                continue
            }
            if (_int(cards[z] / 100) != _int(cards[z + 1] / 100)) {
                z++
                continue
            }
            u = u + 1
            z++
        }
        if (u != 4) {
            z = _int(n)
            while (z <= n + 3) {
                k = z + 1
                while (k <= n + 4) {
                    if (B[z] <= B[k]) {
                        k++
                        continue
                    }
                    x = cards[z]
                    cards[z] = cards[k]
                    B[z] = B[k]
                    cards[k] = x
                    B[k] = cards[k] - 100 * _int(cards[k] / 100)
                    k++
                }
                z++
            }
            x = 0f
            z = _int(n)
            while (z <= n + 3) {
                if (B[z] != B[z + 1]) {
                    z++
                    continue
                }
                x = (x + 11 * 10.0.pow((z - n).toDouble())).toFloat()
                d = _int(cards[z])
                if (u >= 11) {
                    if (u != 11) {
                        if (u > 12) {
                            if (B[z] != B[z - 1]) {
                                fullHouse()
                            } else {
                                u = 17
                                handDescription = "FOUR "
                            }
                        } else {
                            fullHouse()
                        }
                    } else if (B[z] != B[z - 1]) {
                        handDescription = "TWO PAIR, "
                        u = 12
                    } else {
                        handDescription = "THREE "
                        u = 13
                    }
                } else {
                    u = 11
                    handDescription = "A PAIR OF "
                }
                z++
            }
            if (x != 0f) {
                schmaltzHand()
            } else {
                if (B[_int(n)] + 3 == B[_int(n + 3)]) {
                    x = 1111f
                    u = 10
                }
                if (B[_int(n + 1)] + 3 != B[_int(n + 4)]) {
                    schmaltzHand()
                } else if (u != 10) {
                    u = 10
                    x = 11110f
                    schmaltzHand()
                } else {
                    u = 14
                    handDescription = "STRAIGHT"
                    x = 11111f
                    d = _int(cards[_int(n + 4)])
                }
            }
        } else {
            x = 11111f
            d = _int(cards[_int(n)])
            handDescription = "A FLUSH IN"
            u = 15
        }
    }

    fun schmaltzHand() {
        if (u >= 10) {
            if (u != 10) {
                if (u > 12) {
                    return
                }
                if (removeHundreds(d.toLong()) <= 6) {
                    i = 6f
                }
            } else {
                if (i == 1f) {
                    i = 6f
                }
            }
        } else {
            d = _int(cards[_int(n + 4)])
            handDescription = "SCHMALTZ, "
            u = 9
            x = 11000f
            i = 6f
        }
    }

    fun fullHouse() {
        u = 16
        handDescription = "FULL HOUSE, "
    }

    suspend fun playersTurn() {
        g = 0f
        promptAndInputPlayerBet()
    }

    suspend fun readString() = konsole.read()

    suspend fun promptAndInputPlayerBet() {
        println("WHAT IS YOUR BET")
        t = readFloat()
        if (t - _int(t) == 0f) {
            processPlayerBet()
        } else if (k != 0) {
            playerBetInvalidAmount()
        } else if (g != 0f) {
            playerBetInvalidAmount()
        } else if (t.toDouble() == .5) {
        } else {
            playerBetInvalidAmount()
        }
    }

    private suspend fun readFloat(): Float {
        return try {
            readString().toFloat()
        } catch (ex: Exception) {
            println("INVALID INPUT, PLEASE TYPE A FLOAT. ")
            readFloat()
        }
    }

    suspend fun playerBetInvalidAmount() {
        println("NO SMALL CHANGE, PLEASE.")
        promptAndInputPlayerBet()
    }

    suspend fun processPlayerBet() {
        if (humanMoney - g - t >= 0) {
            humanCanAffordBet()
        } else {
            playerBroke()
            promptAndInputPlayerBet()
        }
    }

    suspend fun humanCanAffordBet() {
        if (t != 0f) {
            if (g + t >= k) {
                processComputerMove()
            } else {
                println("IF YOU CAN'T SEE MY BET, THEN FOLD.")
                promptAndInputPlayerBet()
            }
        } else {
            i = 3f
            moveMoneyToPot()
        }
    }

    suspend fun processComputerMove() {
        g = g + t
        if (g == k.toFloat()) {
            moveMoneyToPot()
        } else if (z != 1) {
            if (g > 3 * z) {
                computerRaisesOrSees()
            } else {
                computerRaises()
            }
        } else if (g > 5) {
            if (t <= 25) {
                computerRaisesOrSees()
            } else {
                computerFolds()
            }
        } else {
            v = 5f
            if (g > 3 * z) {
                computerRaisesOrSees()
            } else {
                computerRaises()
            }
        }
    }

    suspend fun computerRaises() {
        v = g - k + random0to10()
        computerMoves()
        println("I'LL SEE YOU, AND RAISE YOU$v")
        k = _int(g + v)
        promptAndInputPlayerBet()
    }

    fun computerFolds() {
        i = 4f
        println("I FOLD.")
    }

    suspend fun computerRaisesOrSees() {
        if (z == 2) {
            computerRaises()
        } else {
            computerSees()
        }
    }

    fun computerSees() {
        println("I'LL SEE YOU.")
        k = _int(g)
        moveMoneyToPot()
    }

    fun moveMoneyToPot() {
        humanMoney = humanMoney - g
        computerMoney = computerMoney - k
        pot = pot + g + k
    }

    fun computerBusted() {
        println("I'M BUSTED.  CONGRATULATIONS!")

    }

    private suspend fun computerBroke() {
        if (playerValuables / 2 == _int(playerValuables / 2).toFloat() && playerBuyBackWatch()) {
        } else if (playerValuables / 3 == _int(playerValuables / 3).toFloat() && playerBuyBackTieRack()) {
        } else {
            computerBusted()
        }
    }

    private fun _int(v: Float): Int {
        return floor(v.toDouble()).toInt()
    }

    private suspend fun playerBuyBackWatch(): Boolean {
        println("WOULD YOU LIKE TO BUY BACK YOUR WATCH FOR $50")
        return if (yesFromPrompt()) {
            computerMoney = computerMoney + 50
            playerValuables = playerValuables / 2
            true
        } else {
            false
        }
    }

    private suspend fun playerBuyBackTieRack(): Boolean {
        println("WOULD YOU LIKE TO BUY BACK YOUR TIE TACK FOR $50")
        return if (yesFromPrompt()) {
            computerMoney = computerMoney + 50
            playerValuables = playerValuables / 3
            true
        } else {
            false
        }
    }

    // line # 3830
    suspend fun playerBroke() {
        println("YOU CAN'T BET WITH WHAT YOU HAVEN'T GOT.")
        if (playerValuables / 2 != _int(playerValuables / 2).toFloat() && playerSellWatch()) {
        } else if (playerValuables / 3 != _int(playerValuables / 3).toFloat() && playerSellTieTack()) {
        } else {
            println("YOUR WAD IS SHOT. SO LONG, SUCKER!")
            // System.exit(0)
        }
    }

    private suspend fun playerSellWatch(): Boolean {
        println("WOULD YOU LIKE TO SELL YOUR WATCH")
        return if (yesFromPrompt()) {
            humanMoney = if (random0to10() < 7) {
                println("I'LL GIVE YOU $75 FOR IT.")
                humanMoney + 75
            } else {
                println("THAT'S A PRETTY CRUMMY WATCH - I'LL GIVE YOU $25.")
                humanMoney + 25
            }
            playerValuables = playerValuables * 2
            true
        } else {
            false
        }
    }

    private suspend fun playerSellTieTack(): Boolean {
        println("WILL YOU PART WITH THAT DIAMOND TIE TACK")
        return if (yesFromPrompt()) {
            humanMoney = if (random0to10() < 6) {
                println("YOU ARE NOW $100 RICHER.")
                humanMoney + 100
            } else {
                println("IT'S PASTE.  $25.")
                humanMoney + 25
            }
            playerValuables = playerValuables * 3
            true
        } else {
            false
        }
    }

}