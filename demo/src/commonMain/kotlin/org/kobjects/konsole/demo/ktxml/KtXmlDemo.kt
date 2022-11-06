package org.kobjects.konsole.demo.ktxml

import org.kobjects.ktxml.mini.MiniXmlPullParser
import org.kobjects.ktxml.api.EventType

suspend fun ktXmlDemo(read: suspend () -> String, write: (String) -> Unit) {
    write("Enter XML an XML snippet see the corresponding KtXml parsing events.")
    while(true) {
        val input = read()
        if (input.isBlank()) {
            break
        }
        val parser = MiniXmlPullParser(input.iterator())
        var sb = StringBuilder()
        while(true) {
            sb.append(parser.positionDescription).append('\n')
            if (parser.eventType == EventType.END_DOCUMENT) {
                break;
            }
            parser.next()
        }
        write(sb.toString())
    }
}