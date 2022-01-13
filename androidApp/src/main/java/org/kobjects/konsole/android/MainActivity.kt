package org.kobjects.konsole.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import org.kobjects.konsole.compose.ComposeKonsole
import org.kobjects.konsole.compose.RenderKonsole
import org.kobjects.konsole.demo.KonsoleDemo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val konsole = ComposeKonsole()

        org.kobjects.konsole.demo.KonsoleDemo(konsole)

        setContent {
            RenderKonsole(konsole = konsole)
        }
    }
}
