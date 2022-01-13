package org.kobjects.konsole

interface Konsole {


    fun print(s: String)

    fun read(value: (String) -> Unit)


}