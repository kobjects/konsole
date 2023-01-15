package org.kobjects.konsole.js

import kotlinx.browser.document
import org.kobjects.konsole.Konsole
import org.w3c.dom.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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

fun br(vararg children: Any) = element("br", *children) as HTMLBRElement
fun textArea(vararg children: Any) = element("textarea", *children) as HTMLTextAreaElement
fun div(vararg children: Any) = element("div", *children) as HTMLDivElement

class HtmlKonsole : Konsole {
    private val input = textArea("disabled" to "disabled", "class" to "konsole-input", "id" to "konsole-input") as HTMLTextAreaElement
    private val enter = div("disabled" to "disabled", "class" to "konsole-button", "»")
    private val inputContainer = div("class" to "konsole-input-container", input, enter)
    private val contentContainer = div("class" to "konsole-content-container")
    val root = div("class" to "konsole-root",  contentContainer, inputContainer)

    private var request: Request? = null

    init {
    //    input.oninput = { onSubmit() }
        enter.onclick = { onSubmit() }
    }

    fun onSubmit() {
        val processing = request ?: return
        val value = input.value
        input.value = ""
        input.disabled = true
        this.request = null

        append(value,  "konsole-bubble-input")
        processing.consumer(value)
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

    override suspend fun read(label: String?) = suspendCoroutine<String> { continuation ->
        if (request != null) {
            throw IllegalStateException("Request pending!")
        }
        input.disabled = false
        input.placeholder = label ?: ""
        input.focus()
        request = Request { continuation.resume(it) }
    }

    class Request(
        val consumer: (String) -> Unit
    )
}