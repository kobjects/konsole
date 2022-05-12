package org.kobjects.konsole

object Ansi {

    fun selectGraphicRendition(vararg code: GraphicRendition): String {
        val sb = StringBuilder("\u001b[")
        if (code.isNotEmpty()) {
            sb.append(code[0].code)
            for (i in 1 until code.size) {
                sb.append(';')
                sb.append(code[i].code)
            }
        }
        sb.append("m")
        return sb.toString()
    }


    enum class GraphicRendition(val code: Int) {
        RESET(0),
        BOLD(1),
        FAINT(2),
        ITALIC(3),


    }
}