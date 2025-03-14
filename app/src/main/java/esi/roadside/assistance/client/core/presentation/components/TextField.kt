package esi.roadside.assistance.client.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.screens.login.InputError
import soup.compose.material.motion.animation.materialFadeIn
import soup.compose.material.motion.animation.materialFadeOut

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = {
        AnimatedVisibility(value.isNotEmpty(), enter = materialFadeIn(), exit = materialFadeOut()) {
            IconButton({ onValueChange("") }) {
                Icon(Icons.Filled.Clear, contentDescription = "Clear")
            }
        }
    },
    error: InputError? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value,
        onValueChange,
        modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null) } },
        trailingIcon = trailingIcon,
        supportingText = error?.let { { Text(stringResource(it.text)) } },
        isError = error != null,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        keyboardActions = keyboardActions,
        singleLine = singleLine
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    passwordHidden: Boolean,
    onPasswordHiddenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.password),
    placeholder: String = stringResource(R.string.password_placeholder),
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = {
        IconButton({ onPasswordHiddenChange(!passwordHidden) }) {
            Icon(
                if (passwordHidden) Icons.Default.Visibility
                else Icons.Default.VisibilityOff,
                contentDescription = "Clear"
            )
        }
    },
    error: InputError? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true
) {
    MyTextField(
        value,
        onValueChange,
        modifier,
        label,
        placeholder,
        leadingIcon,
        trailingIcon,
        error,
        if (passwordHidden) PasswordVisualTransformation() else VisualTransformation. None,
        imeAction,
        keyboardType,
        keyboardActions,
        singleLine
    )
}