package org.kobjects.konsole

interface Konsole {


    fun print(s: String)

    fun input(
        label: String = "",
        validation: (String) -> String = {""},
        handler: (String) -> Unit)


}