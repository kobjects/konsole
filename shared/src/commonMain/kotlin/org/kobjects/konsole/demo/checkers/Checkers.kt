package org.kobjects.konsole.demo.checkers

import org.kobjects.konsole.Konsole
import kotlin.math.abs


class Checkers(
    val konsole: Konsole
) {
    val impossibleMove = Move(-99, 0, 0, 0, 0)
    val data = listOf(1, 0, 1, 0, 0, 0, -1, 0, 0, 1, 0, 0, 0, -1, 0, -1)

    val board = Array(8, { IntArray(8, {0}) })
    val g = -1

    suspend fun run() {
        while (true) {
            game();
        }
    }

    suspend fun game() {
        konsole.write("""
            This is the game of Checkers. The computer is black,
            and you are white. The computer will move first. 
            Squares are referred to by a coordinate system.""".trimIndent().replace('\n', ' '));
        konsole.write("""
            a1 is the lower left corner
            a8 is the upper left corner
            h1 is the lower right corner
            h8 is the upper right corner""".trimIndent())
        var p = 0
        for (x in 0..7) {
            for (y in 0..7) {
                board[x][y] = data[p++ % data.size]
            }
        }

        while (true) {
            val description = computerMove()
            if (description.isEmpty()) {
                konsole.write("You win!")
                break
            }
            konsole.write(description.toString())
            konsole.write(renderBoard())

            var anyComputerPiece = false;
            var anyPlayerPiece = false;
            for (l in 0..7) {
                for (m in 0..7) {
                    if (board[l][m] == 1 || board[l][m] == 2) {
                        anyPlayerPiece = true
                    }
                    if (board[l][m] == -1 || board[l][m] == -2) {
                        anyComputerPiece = true
                    }
                }
            }
            if (!anyPlayerPiece) {
                konsole.write("I win.");
                break;
            }
            if (!anyComputerPiece) {
                konsole.write("You win.");
                break;
            }
            while (true) {
                val problem = playerMove()
                if (problem.isEmpty()) {
                    break
                }
                konsole.write(problem)
            }
        }
    }

    /** Returns a description of the computer move. Empty if impossible */
    fun computerMove(): String {
        var move = impossibleMove

        // Search the board for the best movement
        for (x in 0..7) {
            for (y in 0..7) {
                if (board[x][y] == -1) {	// Piece
                    for (a2 in -1.. 1 step 2) {
                        move = tryComputer(x, y, a2, -1, move);
                    }
                } else if (board[x][y] == -2) {	// King
                    for (a in -1..1 step 2) {
                        for (b in -1..1 step 2) {
                            move = tryComputer(x, y, a, b, move);
                        }
                    }
                }
            }
        }
        if (move == impossibleMove) {
            return ""
        }
        val result = StringBuilder("My move: ${pos(move.x, move.y)}")
        do {
            result.append(' ').append(pos(move.u, move.v))

            if (move.v == 0) {	// Computer reaches the bottom
                board[move.u][move.v] = -2;	// King
                board[move.x][move.y] = 0;
                break;
            }
            board[move.u][move.v] = board[move.x][move.y];	// Move
            board[move.x][move.y] = 0;

            if (abs(move.x - move.u) != 2) {
                break;
            }
            board[(move.u + move.x) / 2][(move.v + move.y) / 2] = 0;	// Capture
            val x = move.u;
            val y = move.v;
            move = impossibleMove
            if (board[x][y] == -1) {
                for (a in -2..2 step 4) {
                    move = moreCaptures(x, y, a, -2, move);
                }
            } else if (board[x][y] == -2) {
                for (a in -2..2 step 4) {
                    for (b in -2..2 step 4) {
                        moreCaptures(x, y, a, b, move);
                    }
                }
            }
        } while (move != impossibleMove)
        return result.toString()
    }

    fun pos(x: Int, y: Int): String {
        return "${('a'.code + x).toChar()}${y + 1}"
    }

    // x,y = origin square
    // a,b = movement direction
    fun tryComputer(x: Int, y: Int, a: Int, b: Int, bestMove: Move): Move {
        var u = x + a
        var v = y + b
        if (u < 0 || u > 7 || v < 0 || v > 7) {
            return bestMove
        }
        if (board[u][v] == 0) {
            return evalMove(x, y, u, v, bestMove)
        }
        if (board[u][v] < 0) {
            // Cannot jump over own pieces
            return bestMove
        }
        u += a
        v += b
        if (u < 0 || u > 7 || v < 0 || v > 7) {
            return bestMove
        }
        if (board[u][v] == 0) {
            return evalMove(x, y, u, v, bestMove)
        }
        return bestMove
    }

    fun evalMove(x: Int, y: Int, u: Int, v: Int, bestMove: Move): Move {
        var score = 0
        if (v == 0 && board[x][y] == -1) {
            score += 2
        }
        if (abs(y - v) == 2) {
            score += 5
        }
        if (y == 7) {
            score -= 2
        }
        if (u == 0 || u == 7) {
            score++
        }
        var c = -1
        while (c <= 1) {
            if (u + c < 0 || u + c > 7 || v + g < 0) {
                c += 2
                continue
            }
            if (board[u + c].get(v + g) < 0) {    // Computer piece
                score++
                c += 2
                continue
            }
            if (u - c < 0 || u - c > 7 || v - g > 7) {
                c += 2
                continue
            }
            if (board[u + c][v + g] > 0
                && (board[u - c][v - g] == 0 || u - c == x && v - g == y)) {
                score -= 2
            }
            c += 2
        }
        return if (score > bestMove.score) Move(score, x, y, u, v) else bestMove
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


    fun moreCaptures(x: Int, y: Int, a: Int, b: Int, bestMove: Move): Move {
        val u = x + a
        val v = y + b
        if (u < 0 || u > 7 || v < 0 || v > 7) {
            return bestMove
        }
        if (board[u][v] == 0 && board[x + a / 2][y + b / 2] > 0) {
            return evalMove(x, y, u, v, bestMove)
        }
        return bestMove
    }

    suspend fun playerMove(): String {
        konsole.write("Your move?")
        val str = konsole.read().trim().lowercase()
        var parts = str.split(" ");
        if (parts.size < 2) {
            return "At least two coordinate pairs expected!"
        }
        val x0 = getX(parts[0])
        val y0 = getY(parts[0])
        if (x0 == -1 || y0 == -1) {
            return "Invalid start coordinate."
        }
        if (board[x0][y0] <= 0) {
            return "The start position must be one of your pieces."
        }
        var piece = board[x0][y0]
        var x = x0
        var y = y0
        var xx = x0
        var yy = y0
        for (i in 1 until parts.size) {
            val part = parts[i].trim()
            val u = getX(part)
            val v = getY(part)
            if (u == xx && v == yy) {
                return "Can't go back to $part"
            }
            if (board[u][v] != 0) {
                return "Target field $part is not empty!"
            }
            if (piece != 2 && v < y) {
                return "Can't move backwards without a king."
            }
            val dx = abs(x - u)
            val dy = abs(y - v)
            if (dx == 1 && dy == 1) {
                if (i != 1) {
                    return "Illegal move $i to $part after a jump."
                }
                if (parts.size != 2) {
                    return "Can't continue after a regular move."
                }
            } else if (dx == 2 && dy == 2) {
                if (board[(x + u) / 2][(y + v) / 2] >= 0) {
                    return "Can't jump to $part over empty fields or own pieces."
                }
            } else {
                return "Illegal move $i by $dx,$dy fields."
            }
            xx = x
            yy = y
            x = u
            y = v
        }
        // Move seems to be plausible.
        x = x0
        y = y0
        board[x][y] = 0
        for (i in 0 until parts.size) {
            val part = parts[i].trim()
            val u = getX(part)
            val v = getY(part)
            if (v == 7) {
                piece = 2
            }
            board[(x + u) / 2][(y + v) / 2] = 0
            if (i == parts.size - 1) {
                board[u][v] = piece
            }
            x = u
            y = v
        }
        return ""
    }



    fun renderBoard(): String {
        val str = StringBuilder("\u3000ａ ｂ ｃ ｄ ｅ ｆ ｇ ｈ\n")
        for (y in 7 downTo 0) {
            str.append((0xff11 + y).toChar())
            for (x in 0..7) {
                str.append(when (board[x][y]) {
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
        return str.toString()
    }


    class Move(
        val score: Int,
        val x: Int,
        val y: Int,
        val u: Int,
        val v: Int
    )


}