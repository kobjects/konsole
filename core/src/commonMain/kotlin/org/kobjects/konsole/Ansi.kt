package org.kobjects.konsole

object Ansi {
    val RESET = selectGraphicRendition(GraphicRendition.RESET)
    val BOLD = selectGraphicRendition(GraphicRendition.BOLD)
    val ITALIC = selectGraphicRendition(GraphicRendition.ITALIC)
    val UNDERLINE = selectGraphicRendition(GraphicRendition.UNDERLINE)
    val NORMAL_INTENSITY = selectGraphicRendition(GraphicRendition.NORMAL_INTENSITY)
    val NORMAL_STYLE = selectGraphicRendition(GraphicRendition.NORMAL_STYLE)
    val NOT_UNDERLINED = selectGraphicRendition(GraphicRendition.NOT_UNDERLINED)
    val PROPORTIONAL = selectGraphicRendition(GraphicRendition.PROPORTIONAL)
    val NOT_PROPORTIONAL = selectGraphicRendition(GraphicRendition.NOT_PROPORTIONAL)

    val FOREGROUND_BLACK = selectGraphicRendition(GraphicRendition.FOREGROUND_BLACK)
    val FOREGROUND_RED = selectGraphicRendition(GraphicRendition.FOREGROUND_RED)
    val FOREGROUND_GREEN = selectGraphicRendition(GraphicRendition.FOREGROUND_GREEN)
    val FOREGROUND_YELLOW = selectGraphicRendition(GraphicRendition.FOREGROUND_YELLOW)
    val FOREGROUND_BLUE = selectGraphicRendition(GraphicRendition.FOREGROUND_BLUE)
    val FOREGROUND_MAGENTA = selectGraphicRendition(GraphicRendition.FOREGROUND_MAGENTA)
    val FOREGROUND_CYAN = selectGraphicRendition(GraphicRendition.FOREGROUND_CYAN)
    val FOREGROUND_WHITE = selectGraphicRendition(GraphicRendition.FOREGROUND_WHITE)
    val FOREGROUND_DEFAULT = selectGraphicRendition(GraphicRendition.FOREGROUND_DEFAULT)

    val BACKGROUND_BLACK = selectGraphicRendition(GraphicRendition.BACKGROUND_BLACK)
    val BACKGROUND_RED = selectGraphicRendition(GraphicRendition.BACKGROUND_RED)
    val BACKGROUND_GREEN = selectGraphicRendition(GraphicRendition.BACKGROUND_GREEN)
    val BACKGROUND_YELLOW = selectGraphicRendition(GraphicRendition.BACKGROUND_YELLOW)
    val BACKGROUND_BLUE = selectGraphicRendition(GraphicRendition.BACKGROUND_BLUE)
    val BACKGROUND_MAGENTA = selectGraphicRendition(GraphicRendition.BACKGROUND_MAGENTA)
    val BACKGROUND_CYAN = selectGraphicRendition(GraphicRendition.BACKGROUND_CYAN)
    val BACKGROUND_WHITE = selectGraphicRendition(GraphicRendition.BACKGROUND_WHITE)
    val BACKGROUND_DEFAULT = selectGraphicRendition(GraphicRendition.BACKGROUND_DEFAULT)

    fun rgbForeground(rgb: Int) =
        selectGraphicRendition(GraphicRendition.FOREGROUND_RGB.ordinal, 2, (rgb shr 16) and 255, (rgb shr 8) and 255, rgb and 255)

    fun rgbBackground(rgb: Int) =
        selectGraphicRendition(GraphicRendition.BACKGROUND_RGB.ordinal, 2, (rgb shr 16) and 255, (rgb shr 8) and 255, rgb and 255)

    fun selectGraphicRendition(vararg code: Int): String {
        val sb = StringBuilder("\u001b[")
        if (code.isNotEmpty()) {
            sb.append(code[0])
            for (i in 1 until code.size) {
                sb.append(';')
                sb.append(code[i])
            }
        }
        sb.append("m")
        return sb.toString()
    }

    fun selectGraphicRendition(vararg code: GraphicRendition): String {
        val ordinals = IntArray(code.size) { code[it].ordinal }
        return selectGraphicRendition(*ordinals)
    }


    enum class GraphicRendition {
        RESET,               //  0
        BOLD,                //  1
        DIM,                 //  2
        ITALIC,              //  3
        UNDERLINE,           //  4
        SLOW_BLINK,          //  5
        RAPID_BLINK,         //  6
        INVERSE,             //  7
        HIDE,                //  8
        CROSSED_OUT,         //  9
        PRIMARY_FONT,        // 10
        ALTERNATIVE_FONT_1,  // 11
        ALTERNATIVE_FONT_2,  // 12
        ALTERNATIVE_FONT_3,  // 13
        ALTERNATIVE_FONT_4,  // 14
        ALTERNATIVE_FONT_5,  // 15
        ALTERNATIVE_FONT_6,  // 16
        ALTERNATIVE_FONT_7,  // 17
        ALTERNATIVE_FONT_8,  // 18
        ALTERNATIVE_FONT_9,  // 19
        GOTHIC,              // 20
        DOUBLE_UNDERLINED,   // 21
        NORMAL_INTENSITY,    // 22
        NORMAL_STYLE,        // 23
        NOT_UNDERLINED,      // 24
        NOT_BLINKING,        // 25
        PROPORTIONAL,        // 26
        NOT_INVERSE,         // 27
        NOT_HIDDEN,          // 28
        NOT_CROSSED_OUT,     // 29
        FOREGROUND_BLACK,    // 30
        FOREGROUND_RED,      // 31
        FOREGROUND_GREEN,    // 32
        FOREGROUND_YELLOW,   // 33
        FOREGROUND_BLUE,     // 34
        FOREGROUND_MAGENTA,  // 35
        FOREGROUND_CYAN,     // 36
        FOREGROUND_WHITE,    // 37
        FOREGROUND_RGB,      // 38
        FOREGROUND_DEFAULT,  // 39
        BACKGROUND_BLACK,    // 40
        BACKGROUND_RED,      // 41
        BACKGROUND_GREEN,    // 42
        BACKGROUND_YELLOW,   // 43
        BACKGROUND_BLUE,     // 44
        BACKGROUND_MAGENTA,  // 45
        BACKGROUND_CYAN,     // 46
        BACKGROUND_WHITE,    // 47
        BACKGROUND_RGB,      // 48
        BACKGROUND_DEFAULT,  // 49
        NOT_PROPORTIONAL,    // 50




    }
}