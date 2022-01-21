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

        append(div( "class" to "konsole-bubble-input", value))

        requests.removeAt(0)
        input.value = ""
        if (requests.isEmpty()) {
             input.disabled = true
            // enter.disabled = true
        }
        request.consumer(value)
    }

    fun append(element: Element) {
        contentContainer.insertBefore(element, contentContainer.firstChild)
    }

    override fun write(text: String) {
        append(div( "class" to "konsole-bubble-output", text))
    }

    override fun readThen(
        label: String,
        validation: (String) -> String,
        consumer: (String) -> Unit
    ) {
        requests.add(Request(label, validation, consumer))
        input.disabled = false
       // enter.disabled = false
    }

    class Request(
        val label: String,
        val validation: (String) -> String,
        val consumer: (String) -> Unit
    )
}