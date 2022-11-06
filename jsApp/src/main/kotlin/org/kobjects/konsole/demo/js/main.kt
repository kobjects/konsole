package org.kobjects.konsole.demo.js

import kotlinx.coroutines.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.kobjects.konsole.demo.Demo
import org.kobjects.konsole.js.HtmlKonsole
import org.kobjects.konsole.js.div
import org.kobjects.konsole.js.element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLUListElement


fun main() {
    val menuDiv = document.getElementById("menu")!! as HTMLDivElement
    val menuList = document.getElementById("menu-list")!! as HTMLUListElement
    val konsoleContainer = document.getElementById("konsole-container")!! as HTMLDivElement
    val title = document.getElementById("title")!! as HTMLDivElement
    val demoMap = mutableMapOf<String, Demo>()

    for (demo in Demo.ALL) {
        val item = element(
            "li", element(
                "a", "href" to  "#${demo.name}", demo.name))
        menuList.appendChild(item)
        demoMap.put("#" + demo.name.replace(" ", "%20"), demo)
    }

    menuDiv.appendChild(
        div(
            element("small","*) Well ${Demo.ALL.size} actually... ¯\\_(ツ)_/¯")))

    fun updateLocation() {
        val hash = window.location.hash
        val demo = demoMap[hash]
        if (demo == null) {
            menuDiv.style.display = "flex"
            konsoleContainer.style.display = "none"
            title.innerText = "101* KMP Konsole Apps"
        } else {
            menuDiv.style.display = "none"
            konsoleContainer.style.display = "flex"
            title.innerText = ""
            title.appendChild(element("big", element("a", "href" to "#", "\u00a0‹\u00a0\u00a0")))
            title.appendChild(document.createTextNode(demo.name))

            val konsole = HtmlKonsole()
            konsoleContainer.innerText = ""
            konsoleContainer.appendChild(konsole.root)
            GlobalScope.launch {
                demo.run( { konsole.read() }, { konsole.write(it) } )
                window.location.hash = "#"
            }
        }
    }

    window.onhashchange = {
        updateLocation()
    }


    updateLocation()
}