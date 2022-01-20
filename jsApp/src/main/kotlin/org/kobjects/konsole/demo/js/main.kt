package org.kobjects.konsole.demo.js

import kotlinx.coroutines.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.kobjects.konsole.demo.Demo
import org.kobjects.konsole.js.HtmlKonsole
import org.kobjects.konsole.js.element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLUListElement
import org.w3c.dom.get
import kotlin.coroutines.*



fun main() {
    val menuDiv = document.getElementById("menu")!! as HTMLDivElement
    val menuList = document.getElementById("menuList")!! as HTMLUListElement
    val konsoleDiv = document.getElementById("konsole")!! as HTMLDivElement
    val konsoleTitle = document.getElementById("konsole-title")!! as HTMLDivElement
    val konsoleContent = document.getElementById("konsole-content")!! as HTMLDivElement
    val demoMap = mutableMapOf<String, Demo>()

    for (demo in Demo.ALL) {
        val item = element(
            "li", element(
                "a", "href" to  "#${demo.name}", demo.name))
        menuList.appendChild(item)
        demoMap.put("#" + demo.name.replace(" ", "%20"), demo)
    }


    fun updateLocation() {
        val hash = window.location.hash
        val demo = demoMap[hash]
        if (demo == null) {
            menuDiv.style.display = ""
            konsoleDiv.style.display = "none"
        } else {
            menuDiv.style.display = "none"
            konsoleDiv.style.display = "flex"
            konsoleContent.innerText = ""
            konsoleTitle.innerText = demo.name
            val konsole = HtmlKonsole()
            konsoleContent.appendChild(konsole.root)
            GlobalScope.launch {
                demo.run(konsole)
            }
        }
    }

    window.onhashchange = {
        updateLocation()
    }


    updateLocation()
}