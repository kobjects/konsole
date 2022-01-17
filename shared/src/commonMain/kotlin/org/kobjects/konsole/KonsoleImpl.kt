package org.kobjects.konsole

class KonsoleImpl : Konsole {
    var writeFunction: (String) -> Unit = {}
    var readFunction: (String, (String) -> String, (String) -> Unit) -> Unit = {
        label, validation, consumer ->
    }

    override fun write(s: String) {
        println("write $s")
        writeFunction(s)
    }
    override fun readThen(
        label: String,
        validation: (String) -> String,
        consumer: (String) -> Unit
    ) = readFunction(label, validation, consumer)
}