package org.kobjects.konsole.demo.banner

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

suspend fun banner(read: suspend (String?) -> String, write: (String) -> Unit ) {
    while (true) {
        val statement = read("Text?").uppercase()

        if (statement.isBlank()) {
            break
        }

        val sb = StringBuilder()

        var i = 0
        while (i < statement.length) {
            val c = statement[i++]
            // surrogate pair handling
            val cp = if (c in '\ud800' .. '\ud8ff') "" + c + statement[i++] else "" + c
            sb.append((FONT[cp] ?: FONT["?"]!!).trimIndent());
            sb.append("\n⬜⬜⬜⬜⬜⬜⬜\n")
        }
        write(sb.toString())
    }
}

val FONT = mapOf(
    " " to """
        ⬜⬜⬜⬜⬜⬜⬜
        ⬜⬜⬜⬜⬜⬜⬜
        ⬜⬜⬜⬜⬜⬜⬜""",
    "A" to """
        ⬛⬛⬛⬛⬛⬛⬜
        ⬜⬜⬜⬛⬜⬜⬛
        ⬜⬜⬜⬛⬜⬜⬛
        ⬛⬛⬛⬛⬛⬛⬜""",
    "B" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬛⬜⬜⬛⬜⬜⬛
        ⬛⬜⬜⬛⬜⬜⬛
        ⬜⬛⬛⬜⬛⬛⬜""",
    "C" to """
        ⬜⬛⬛⬛⬛⬛⬜
        ⬛⬜⬜⬜⬜⬜⬛
        ⬛⬜⬜⬜⬜⬜⬛""",
    "D" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬛⬜⬜⬜⬜⬜⬛
        ⬛⬜⬜⬜⬜⬜⬛
        ⬜⬛⬛⬛⬛⬛⬜""",
    "E" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬛⬜⬜⬛⬜⬜⬛
        ⬛⬜⬜⬜⬜⬜⬛""",
    "F" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬜⬜⬜⬛⬜⬜⬛
        ⬜⬜⬜⬜⬜⬜⬛""",
    "G" to """
        ⬜⬛⬛⬛⬛⬛⬜
        ⬛⬜⬜⬜⬜⬜⬛
        ⬛⬜⬜⬛⬜⬜⬛
        ⬛⬛⬛⬛⬜⬜⬜""",
    "H" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬜⬜⬜⬛⬜⬜⬜
        ⬜⬜⬜⬛⬜⬜⬜
        ⬛⬛⬛⬛⬛⬛⬛""",
    "I" to """
        ⬛⬛⬛⬛⬛⬛⬛""",
    "J" to """
        ⬜⬛⬛⬜⬜⬜⬜
        ⬛⬜⬜⬜⬜⬜⬜
        ⬛⬜⬜⬜⬜⬜⬜
        ⬜⬛⬛⬛⬛⬛⬛""",
    "K" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬜⬜⬜⬛⬜⬜⬜
        ⬜⬛⬛⬜⬛⬛⬜
        ⬛⬜⬜⬜⬜⬜⬛""",
    "L" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬛⬜⬜⬜⬜⬜⬜
        ⬛⬜⬜⬜⬜⬜⬜""",
    "M" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬜⬜⬜⬜⬜⬛⬜
        ⬜⬜⬜⬛⬛⬜⬜
        ⬜⬜⬜⬜⬜⬛⬜
        ⬛⬛⬛⬛⬛⬛⬛""",
    "N" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬜⬜⬜⬜⬛⬜⬜
        ⬜⬜⬜⬛⬜⬜⬜
        ⬛⬛⬛⬛⬛⬛⬛""",
    "O" to """
        ⬜⬛⬛⬛⬛⬛⬜
        ⬛⬜⬜⬜⬜⬜⬛
        ⬛⬜⬜⬜⬜⬜⬛
        ⬜⬛⬛⬛⬛⬛⬜""",
    "P" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬜⬜⬜⬛⬜⬜⬛
        ⬜⬜⬜⬛⬜⬜⬛
        ⬜⬜⬜⬜⬛⬛⬜""",
    "Q" to """
        ⬜⬛⬛⬛⬛⬛⬜
        ⬛⬜⬜⬜⬜⬜⬛
        ⬛⬜⬛⬜⬜⬜⬛
        ⬜⬛⬛⬛⬛⬛⬜""",
    "R" to """
        ⬛⬛⬛⬛⬛⬛⬛
        ⬜⬜⬛⬛⬜⬜⬛
        ⬜⬛⬜⬛⬜⬜⬛
        ⬛⬜⬜⬜⬛⬛⬜""",
    "S" to """
        ⬛⬜⬜⬜⬛⬛⬜
        ⬛⬜⬜⬛⬜⬜⬛
        ⬛⬜⬜⬛⬜⬜⬛
        ⬜⬛⬛⬜⬜⬜⬛""",
    "T" to """
        ⬜⬜⬜⬜⬜⬜⬛
        ⬜⬜⬜⬜⬜⬜⬛
        ⬛⬛⬛⬛⬛⬛⬛
        ⬜⬜⬜⬜⬜⬜⬛
        ⬜⬜⬜⬜⬜⬜⬛""",
    "U" to """
        ⬜⬛⬛⬛⬛⬛⬛
        ⬛⬜⬜⬜⬜⬜⬜
        ⬛⬜⬜⬜⬜⬜⬜
        ⬜⬛⬛⬛⬛⬛⬛""",
    "V" to """
        ⬜⬜⬜⬜⬛⬛⬛
        ⬜⬜⬛⬛⬜⬜⬜
        ⬛⬛⬜⬜⬜⬜⬜
        ⬜⬜⬛⬛⬜⬜⬜
        ⬜⬜⬜⬜⬛⬛⬛""",
    "W" to """
        ⬜⬛⬛⬛⬛⬛⬛
        ⬛⬜⬜⬜⬜⬜⬜
        ⬜⬛⬛⬛⬜⬜⬜
        ⬛⬜⬜⬜⬜⬜⬜
        ⬜⬛⬛⬛⬛⬛⬛""",
    "X" to """
        ⬛⬛⬜⬜⬜⬛⬛
        ⬜⬜⬛⬜⬛⬜⬜
        ⬜⬜⬜⬛⬜⬜⬜
        ⬜⬜⬛⬜⬛⬜⬜
        ⬛⬛⬜⬜⬜⬛⬛""",
    "Y" to """
        ⬜⬜⬜⬜⬜⬛⬛
        ⬜⬜⬜⬜⬛⬜⬜
        ⬛⬛⬛⬛⬜⬜⬜
        ⬜⬜⬜⬜⬛⬜⬜
        ⬜⬜⬜⬜⬜⬛⬛""",
    "Z" to """
        ⬛⬛⬜⬜⬜⬜⬛
        ⬛⬜⬛⬛⬜⬜⬛
        ⬛⬜⬜⬛⬛⬜⬛
        ⬛⬜⬜⬜⬜⬛⬛""",
    "." to """
        ⬛⬛⬜⬜⬜⬜⬜
        ⬛⬛⬜⬜⬜⬜⬜""",
    "?" to """
        ⬜⬜⬜⬜⬜⬛⬜
        ⬜⬜⬜⬜⬜⬜⬛
        ⬛⬜⬛⬛⬜⬜⬛
        ⬜⬜⬜⬜⬛⬛⬜""",
    "!" to """
        ⬛⬜⬛⬛⬛⬛⬛""",
    "\uD83D\uDE00" to """
       ⬜🟨🟨🟨🟨🟨⬜
       🟨🟨⬛🟨🟨🟨🟨
       🟨⬛🟨🟨🟨⬛🟨
       🟨⬛🟨🟨🟨🟨🟨
       🟨⬛🟨🟨🟨⬛🟨
       🟨🟨⬛🟨🟨🟨🟨
       ⬜🟨🟨🟨🟨🟨⬜"""
)
