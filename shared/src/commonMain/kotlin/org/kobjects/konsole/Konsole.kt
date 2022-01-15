package org.kobjects.konsole

interface Konsole {


    fun write(s: String)

    suspend fun read(
        label: String = "",
        validation: (String) -> String = {""}
    ): String


}