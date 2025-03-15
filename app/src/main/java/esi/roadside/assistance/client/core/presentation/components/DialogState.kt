package esi.roadside.assistance.client.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class DialogState internal constructor() {
    var title by mutableStateOf("")
    var text by mutableStateOf("")
    var visible by mutableStateOf(false)
    fun show() {
        visible = true
    }
    fun hide() {
        visible = false
    }
}

@Composable
fun rememberDialogState(): DialogState {
    return remember {
        DialogState()
    }
}