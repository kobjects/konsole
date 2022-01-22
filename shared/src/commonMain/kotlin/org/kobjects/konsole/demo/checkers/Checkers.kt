package org.kobjects.konsole.demo.checkers

import org.kobjects.konsole.Konsole
import kotlin.math.abs


class Checkers(
    val konsole: Konsole
) {

    val r = mutableListOf(-99, 0, 0, 0, 0)
    var s = List(8, { MutableList(8, {0}) })
    var g = -1
    var data = listOf(1, 0, 1, 0, 0, 0, -1, 0, 0, 1, 0, 0, 0, -1, 0, -1, 15)
    var p = 0;
    var q = 0;

    var a = 0
    var b = 0

    var e = 0
    var h = 0
    var i = 0

    var x = 0
    var y = 0
    var u = 0
    var v = 0

    suspend fun run() {
        konsole.write("""
            This is the game of Checkers. The computer is X,
            and you are O. The computer will move first. 
            squares are referred to by a coordinate system.""".trimIndent().replace('\n', ' '));
        konsole.write("""
            a1 is the lower left corner
            a8 is the upper left corner
            h1 is the lower right corner
            h8 is the upper right corner""".trimIndent())
        konsole.write("""
            The computer will type '+TO' when you have another 
            jump. Type two negative numbers if you cannot jump.""".trimIndent().replace('\n', ' '))
        for (x in 0..7) {
            for (y in 0..7) {
                if (data[p] == 15) {
                    p = 0;
                }
                s[x][y] = data[p];
                p++;
            }
        }

        while (true) {

            // Search the board for the best movement
            for (x2 in 0..7) {
                for (y2 in 0..7) {
                    x = x2
                    y = y2
                    if (s[x][y] > -1) {
                        continue;
                    }
                    if (s[x][y] == -1) {	// Piece
                        for (a2 in -1.. 1 step 2) {
                            a = a2
                            b = g;	// Only advances
                            try_computer();
                        }
                    } else if (s[x][y] == -2) {	// King
                        for (a in -1..1 step 2) {
                            for (b in -1..1 step 2) {
                                this.a = a
                                this.b = b
                                try_computer();
                            }
                        }
                    }
                }
            }
            if (r[0] == -99) {
                konsole.write("YOU WIN.\n");
                break;
            }
            konsole.write("FROM " + r[1] + "," + r[2] + " TO " + r[3] + "," + r[4]);
            r[0] = -99;
            while (true) {
                if (r[4] == 0) {	// Computer reaches the bottom
                    s[r[3]][r[4]] = -2;	// King
                    break;
                }
                s[r[3]][r[4]] = s[r[1]][r[2]];	// Move
                s[r[1]][r[2]] = 0;
                if (abs(r[1] - r[3]) == 2) {
                    s[(r[1] + r[3]) / 2][(r[2] + r[4]) / 2] = 0;	// Capture
                    x = r[3];
                    y = r[4];
                    if (s[x][y] == -1) {
                        b = -2;
                        for (a in -2..2 step 4) {
                            this.a = a
                            more_captures();
                        }
                    } else if (s[x][y] == -2) {
                        for (a2 in -2..2 step 4) {
                            for (b2 in -2..2 step 4) {
                                a = a2
                                b = b2
                                more_captures();
                            }
                        }
                    }
                    if (r[0] != -99) {
                        print(" TO " + r[3] + "," + r[4]);
                        r[0] = -99;
                        continue;
                    }
                }
                break;
            }
            val str = StringBuilder("\u3000ａ ｂ ｃ ｄ ｅ ｆ ｇ ｈ\n")
            for (y2 in 7 downTo 0) {
                y = y2
                str.append((0xff11 + y).toChar())
                for (x2 in 0..7) {
                    x = x2
                    str.append(when (s[x][y]) {
                        0 -> if (((x + y) % 2) == 0) "\uD83D\uDFE9" else "🟧"
                        1 -> "⚪"
                        -1 -> "⚫"
                        -2 -> "\uD83D\uDDA4"
                        2 -> "\uD83E\uDD0D"
                        else -> throw IllegalStateException()
                    })
            }
                str.append((0xff11 + y).toChar())
                str.append("\n");
            }
            str.append("\u3000ａ ｂ ｃ ｄ ｅ ｆ ｇ ｈ")
            konsole.write(str.toString())
            var z = 0;
            var t = 0;
            for (l in 0..7) {
                for (m in 0..7) {
                if (s[l][m] == 1 || s[l][m] == 2)
                    z = 1;
                if (s[l][m] == -1 || s[l][m] == -2)
                    t = 1;
                }
            }
            if (z != 1) {
                konsole.write("I WIN.\n");
                break;
            }
            if (t != 1) {
                konsole.write("YOU WIN.\n");
                break;
            }
            do {
                konsole.write("FROM");
                val str = konsole.read().trim()
                h = getY(str)
                e = getX(str)
                x = e
                y = h
                konsole.write("x: $x, y: $y")
            } while (x < 0 || y < 0 || s[x][y] <= 0)
            do {
                konsole.write("TO");
                val str = konsole.read().trim()
                b = getY(str)
                a = getX(str)
                x = a
                y = b;
                if (s[x][y] == 0 && abs(a - e) <= 2 &&  abs(a - e) == abs(b - h))
                    break;
                konsole.write("WHAT?\n");
            } while (true)
            i = 46;
            do {
                s[a][b] = s[e][h]
                s[e][h] = 0;
                if (abs(e - a) != 2)
                    break;
                s[(e + a) / 2][(h + b) / 2] = 0;
                var a1 = 0
                var b1 = 0
                while (true) {
                    konsole.write("+TO");
                    val str = konsole.read()
                    b1 = getY(str)
                    a1 = getX(str)
                    if (a1 < 0)
                        break;
                    if (s[a1][b1] == 0 &&  abs(a1 - a) == 2 &&  abs(b1 - b) == 2)
                        break;
                }
                if (a1 < 0)
                    break;
                e = a;
                h = b;
                a = a1;
                b = b1;
                i += 15;
            } while (true);
            if (b == 7)	// Player reaches top
                s[a][b] = 2;	// Convert to king
        }
    }

    // x,y = origin square
    // a,b = movement direction
    fun try_computer() {
        u = x + a
        v = y + b
        if (u < 0 || u > 7 || v < 0 || v > 7) return
        if (s[u][v] == 0) {
            eval_move()
            return
        }
        if (s[u][v] < 0) // Cannot jump over own pieces
            return
        u += a
        u += b
        if (u < 0 || u > 7 || v < 0 || v > 7) return
        if (s[u][v] == 0) eval_move()
    }


    fun eval_move() {
        if (v == 0 && s[x][y] == -1) q += 2
        if (abs(y - v) == 2) q += 5
        if (y == 7) q -= 2
        if (u == 0 || u == 7) q++
        var c = -1
        while (c <= 1) {
            if (u + c < 0 || u + c > 7 || v + g < 0) {
                c += 2
                continue
            }
            if (s[u + c].get(v + g) < 0) {    // Computer piece
                q++
                c += 2
                continue
            }
            if (u - c < 0 || u - c > 7 || v - g > 7) {
                c += 2
                continue
            }
            if (s[u + c].get(v + g) > 0 && (s[u - c].get(v - g) == 0 || u - c == x && v - g == y)) {
                q -= 2
            }
            c += 2
        }
        if (q > r[0]) {    // Best movement so far?
            r[0] = q // Take note of score
            r[1] = x // Origin square
            r[2] = y
            r[3] = u // Target square
            r[4] = v
        }
        q = 0
    }

    fun getX(pos: String): Int {
        if (pos.length != 2) {
            return -1
        }
        val n = pos[0].lowercaseChar() - 'a'
        return if (n < 0 || n > 7) -1 else n
    }

    fun getY(pos: String): Int {
        if (pos.length != 2) {
            return -1
        }
        val n = pos[1] - '1'
        return if (n < 0 || n > 7) -1 else n
    }


    fun more_captures() {
        u = x + a
        v = y + b
        if (u < 0 || u > 7 || v < 0 || v > 7) {
            return
        }
        if (s[u][v] == 0 && s[x + a / 2][y + b / 2] > 0) {
            eval_move()
        }
    }


    companion object {

        fun tab(space: Int): String {
            var str = StringBuilder();
            for (i in 1..space) {
                str.append(' ')
            }
            return str.toString();
        }
    }

}