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

    fun show(title: String, demo: suspend (read: suspend (String?) -> String, write: (String) -> Unit) -> Unit) {
        this.title.value = title
        konsole.value = ComposeKonsole()
        viewModelScope.launch {
            demo( {konsole.value.read(it)}, {konsole.value.write(it)} )
            this@DemoViewModel.title.value = ""
        }
    }
}
