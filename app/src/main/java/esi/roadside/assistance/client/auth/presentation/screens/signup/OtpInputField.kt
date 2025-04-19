package esi.roadside.assistance.client.auth.presentation.screens.signup

import android.view.KeyEvent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

@Composable
fun OtpInputField(
    number: Int?,
    enabled: Boolean,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onNumberChanged: (Int?) -> Unit,
    onKeyboardBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text by remember(number) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString().orEmpty(),
                selection = TextRange(
                    index = if(number != null) 1 else 0
                )
            )
        )
    }
    var isFocused by remember {
        mutableStateOf(false)
    }
    val borderStroke by animateDpAsState(if (isFocused) 2.dp else 0.dp)

    Box(
        modifier = modifier
            .border(
                width = borderStroke,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
            .background(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                val newNumber = newText.text
                if(newNumber.length <= 1 && newNumber.isDigitsOnly()) {
                    onNumberChanged(newNumber.toIntOrNull())
                }
            },
            enabled = enabled,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier
                .padding(10.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                    onFocusChanged(it.isFocused)
                }
                .onKeyEvent { event ->
                    val didPressDelete = event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                    if(didPressDelete && number == null) {
                        onKeyboardBack()
                    }
                    false
                },
            decorationBox = { innerBox ->
                innerBox()
                if(!isFocused && number == null) {
                    Text(
                        text = "-",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                    )
                }
            }
        )
    }
}