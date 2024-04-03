import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kobjects.konsole.composedemo.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "konsole") {
        App()
    }
}