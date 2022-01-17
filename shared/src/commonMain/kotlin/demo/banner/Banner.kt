package demo.banner

import org.kobjects.konsole.Konsole

/**
 * Game of Banner
 *
 * Based on the BASIC game of Banner here
 * https://github.com/coding-horror/basic-computer-games/blob/main/06%20Banner/banner.bas
 *
 * Converted from BASIC to Java by Darren Cardenas.
 *
 * Converted from Java to Kotlin by Stefan Haustein
 */

suspend fun banner(konsole: Konsole) {
    while (true) {
        konsole.write("Text?")
        val statement = konsole.read().uppercase()
        val sb = StringBuilder()

        for (letter in statement) {
            var data = CHARACTER_DATA[letter] ?: CHARACTER_DATA['?']!!
            for (pattern in data) {
                var mask = 512
                while (mask != 0) {
                    sb.append(if ((pattern and mask) != 0) '⬛' else '⬜')
                    mask /= 2
                }
                sb.append('\n')
            }
        }
        konsole.write(sb.toString())
    }
}

val CHARACTER_DATA = mapOf(
    ' ' to intArrayOf(0, 0, 0, 0),
    'A' to intArrayOf(
        0,
        0b00111111000,
        0b00001000100,
        0b00001000100,
        0b00001000100,
        0b00111111000
    ),
    'B' to intArrayOf(
        0,
        0b00111111100,
        0b00100100100,
        0b00100100100,
        0b00100100100,
        0b00011011000,
    ),
    'C' to intArrayOf(
        0,
        0b00011111000,
        0b00100000100,
        0b00100000100,
        0b00010001000,
    ),
    'D' to intArrayOf(
        0,
        0b00111111100,
        0b00100000100,
        0b00100000100,
        0b00100000100,
        0b00011111000,
    ),
    'E' to intArrayOf(
        0,
        0b00111111100,
        0b00100100100,
        0b00100100100,
        0b00100000100,
    ),
    'F' to intArrayOf(
        0,
        0b00111111100,
        0b00000100100,
        0b00000100100,
        0b00000000100,
    ),
    'G' to intArrayOf(
        0,
        0b00011111000,
        0b00100000100,
        0b00100100100,
        0b00111101000,
    ),
    'H' to intArrayOf(
        0,
        0b00111111100,
        0b00000100000,
        0b00000100000,
        0b00111111100,
    ),
    'I' to intArrayOf(
        0,
        0b00111111100,
    ),
    'J' to intArrayOf(
        0,
        0b00010000000,
        0b00100000000,
        0b00100000100,
        0b00111111100,
    ),
    'K' to intArrayOf(
        0,
        0b00111111100,
        0b00000100000,
        0b00001010000,
        0b00110101100,
    ),
    'L' to intArrayOf(
        0,
        0b00111111100,
        0b00100000000,
        0b00100000000,
        0b00100000000,
    ),
    'M' to intArrayOf(
        0,
        0b00111111100,
        0b00000001000,
        0b00000110000,
        0b00000001000,
        0b00111111100
    ),
    'N' to intArrayOf(
        0,
        0b00111111100,
        0b00000001000,
        0b00000010000,
        0b00000100000,
        0b00111111100
    ),
    'O' to intArrayOf(
        0,
        0b00011111000,
        0b00100000100,
        0b00100000100,
        0b00100000100,
        0b00011111000
    ),
    'P' to intArrayOf(
        0,
        0b00111111100,
        0b00100100100,
        0b00000100100,
        0b00000011000
    ),
    'Q' to intArrayOf(
        0,
        0b00011111000,
        0b00100000100,
        0b00101000100,
        0b00010000100,
        0b00101111000
    ),
    'R' to intArrayOf(
        0,
        0b00111111100,
        0b00000100100,
        0b00001100100,
        0b00010100100,
        0b00100011000
    ),
    'S' to intArrayOf(
        0,
        0b00010011000,
        0b00100100100,
        0b00100100100,
        0b00100100100,
        0b00011001000
    ),
    'T' to intArrayOf(
        0,
        0b00000000100,
        0b00000000100,
        0b00111111100,
        0b00000000100,
        0b00000000100
    ),
    'U' to intArrayOf(
        0,
        0b00011111100,
        0b00100000000,
        0b00100000000,
        0b00100000000,
        0b00011111100
    ),
    'V' to intArrayOf(
        0,
        0b00001111100,
        0b00010000000,
        0b00100000000,
        0b00010000000,
        0b00001111100
    ),
    'W' to intArrayOf(
        0,
        0b00011111100,
        0b00100000000,
        0b00011000000,
        0b00100000000,
        0b00011111100
    ),
    'X' to intArrayOf(
        0,
        0b00110001100,
        0b00001010000,
        0b00000100000,
        0b00001010000,
        0b00110001100
    ),
    'Y' to intArrayOf(
        0,
        0b00000001100,
        0b00000010000,
        0b00111100000,
        0b00001010000,
        0b00000001100
    ),
    'Z' to intArrayOf(
        0,
        0b00100000100,
        0b00110000100,
        0b00101100100,
        0b00100010100,
        0b00100001100
    ),

    '.' to intArrayOf(
        0,
        0b00110000000,
        0b00110000000
    ),
    '?' to intArrayOf(
        0,
        0b00000001000,
        0b00000000100,
        0b00101100100,
        0b00000100100,
        0b00000011000
    )
)

