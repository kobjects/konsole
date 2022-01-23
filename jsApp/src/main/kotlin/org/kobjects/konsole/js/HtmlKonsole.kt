package org.kobjects.konsole.js

import kotlinx.browser.document
import org.kobjects.konsole.Konsole
import org.w3c.dom.*

fun element(name: String, vararg children: Any): HTMLElement {
    val element = document.createElement(name) as HTMLElement
    for (child in children) {
        if (child is Pair<*, *>) {
            element.setAttribute(child.first.toString(), child.second.toString())
        } else if (child is Node) {
            element.appendChild(child)
        } else {
            element.appendChild(document.createTextNode(child.toString()))
        }
    }
    return element
}

fun textArea(vararg children: Any) = element("textarea", *children) as HTMLTextAreaElement
fun div(vararg children: Any) = element("div", *children) as HTMLDivElement

class HtmlKonsole : Konsole {
    private val input = textArea("disabled" to "disabled", "class" to "konsole-input") as HTMLTextAreaElement
    private val enter = div("disabled" to "disabled", "class" to "konsole-button", "Enter")
    private val inputContainer = div("class" to "konsole-input-container", input, enter)
    private val contentContainer = div("class" to "konsole-content-container")
    val root = div("class" to "konsole-root",  contentContainer, inputContainer)

    private val requests = mutableListOf<Request>()

    init {
    //    input.oninput = { onSubmit() }
        enter.onclick = { onSubmit() }
    }

    fun onSubmit() {
        if (requests.isEmpty()) {
            return
        }
        val request = requests[0]
        val value = input.value

        append(value,  "konsole-bubble-input")

        requests.removeAt(0)
        input.value = ""
        if (requests.isEmpty()) {
             input.disabled = true
            // enter.disabled = true
        }
        request.consumer(value)
    }

    fun append(text: String, cssClass: String) {
        val adjusted = StringBuilder()
        var i = 0;
        while (i < text.length) {
            var c = text[i++]
            adjusted.append(c)
            var cp = c.code
            if (cp in 0xd800..0xdbff) {
                var d = text[i++]
                adjusted.append(d)
                cp = 0x10000 + (cp - 0xd800) * 0x400 + (d.code - 0xdc00)
            }
            if (cp in 0x2500..0x26ff || cp > 0x1f600) {
                adjusted.append(' ')
            }
        }
        contentContainer.insertBefore(
            div("class" to cssClass, adjusted.toString()),
            contentContainer.firstChild)
    }

    override fun write(text: String) {
        append(text, "konsole-bubble-output")
    }

    override fun readThen(
        label: String,
        validation: (String) -> String,
        consumer: (String) -> Unit
    ) {
        requests.add(Request(label, validation, consumer))
        input.disabled = false
        input.focus()
       // enter.disabled = false
    }

    class Request(
        val label: String,
        val validation: (String) -> String,
        val consumer: (String) -> Unit
    )
}