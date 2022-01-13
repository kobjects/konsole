package org.kobjects.konsole.demo

import org.kobjects.konsole.Konsole

fun KonsoleDemo(konsole: Konsole) {
    konsole.print("Hello World!")
    inputLoop(konsole)
}


fun inputLoop(konsole: Konsole) {
    konsole.print("What's your name?")

    konsole.read { it ->
        konsole.print("Hello $it")
        inputLoop(konsole)
    }

}