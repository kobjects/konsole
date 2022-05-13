package org.kobjects.konsole.compose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import org.kobjects.konsole.Ansi

object AnsiConverter {

    fun ansiToAnnotatedString(ansi: String): AnnotatedString {
        var pos = 0
        val len = ansi.length
        var style = Style()
        val sb = AnnotatedString.Builder()
        while (pos < len) {
            val esc = ansi.indexOf("\u001b[", pos)
            if (esc == -1) {
                break
            }
            appendStyled(sb, ansi.substring(pos, esc), style)
            pos = esc + 2
            while (pos < len && ansi[pos] <= 'A') {
                pos++
            }
            for (code in ansi.substring(esc + 2, pos).split(";")) {
                when (Ansi.GraphicRendition.values()[code.toInt()]) {
                    Ansi.GraphicRendition.RESET -> style = Style()

                    Ansi.GraphicRendition.BOLD -> style.bold = true
                    Ansi.GraphicRendition.ITALIC -> style.italic = true
                    Ansi.GraphicRendition.UNDERLINE -> style.underline = true

                    Ansi.GraphicRendition.NORMAL_INTENSITY -> style.bold = false
                    Ansi.GraphicRendition.NOT_PROPORTIONAL -> style.monospace = true
                    Ansi.GraphicRendition.PROPORTIONAL -> style.monospace = false

                    Ansi.GraphicRendition.FOREGROUND_BLACK -> style.color = Color.Black
                    Ansi.GraphicRendition.FOREGROUND_RED -> style.color = Color.Red
                    Ansi.GraphicRendition.FOREGROUND_GREEN -> style.color = Color.Green
                    Ansi.GraphicRendition.FOREGROUND_YELLOW -> style.color = Color.Yellow
                    Ansi.GraphicRendition.FOREGROUND_BLUE -> style.color = Color.Blue
                    Ansi.GraphicRendition.FOREGROUND_MAGENTA -> style.color = Color.Magenta
                    Ansi.GraphicRendition.FOREGROUND_CYAN -> style.color = Color.Cyan
                    Ansi.GraphicRendition.FOREGROUND_WHITE -> style.color = Color.White
                    Ansi.GraphicRendition.FOREGROUND_DEFAULT -> style.color = Color.Unspecified

                    Ansi.GraphicRendition.BACKGROUND_BLACK -> style.background = Color.Black
                    Ansi.GraphicRendition.BACKGROUND_RED -> style.background = Color.Red
                    Ansi.GraphicRendition.BACKGROUND_GREEN -> style.background = Color.Green
                    Ansi.GraphicRendition.BACKGROUND_YELLOW -> style.background = Color.Yellow
                    Ansi.GraphicRendition.BACKGROUND_BLUE -> style.background = Color.Blue
                    Ansi.GraphicRendition.BACKGROUND_MAGENTA -> style.background = Color.Magenta
                    Ansi.GraphicRendition.BACKGROUND_CYAN -> style.background = Color.Cyan
                    Ansi.GraphicRendition.BACKGROUND_WHITE -> style.background = Color.White
                    Ansi.GraphicRendition.BACKGROUND_DEFAULT -> style.background = Color.Unspecified
                }
            }
            pos++
        }
        appendStyled(sb, ansi.substring(pos, len), style)
        return sb.toAnnotatedString()
    }

    fun appendStyled(sb: AnnotatedString.Builder, s: String, style: Style) {
        val start = sb.length
        sb.append(s)
        sb.addStyle(style.toSpanStyle(), start, start + s.length)
    }

    class Style {
        var bold = false
        var underline = false
        var italic = false
        var monospace = false
        var color: Color = Color.Unspecified
        var background: Color = Color.Unspecified

        fun toSpanStyle() = SpanStyle(
            fontWeight = if (bold) FontWeight.Bold else null,
            fontFamily = if (monospace) FontFamily.Monospace else FontFamily.Default,
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
            textDecoration = if (underline) TextDecoration.Underline else null,
            color = color,
            background = background
        )
    }

}