package org.kobjects.konsole.android;

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kobjects.konsole.Konsole
import org.kobjects.konsole.compose.ComposeKonsole

class DemoViewModel : ViewModel() {

    val title = mutableStateOf("")
    val konsole = mutableStateOf<ComposeKonsole>(ComposeKonsole())

    fun show(title: String, demo: suspend (Konsole) -> Unit) {
        this.title.value = title
        konsole.value = ComposeKonsole()
        viewModelScope.launch {
            demo(konsole.value)
        }
    }
}
