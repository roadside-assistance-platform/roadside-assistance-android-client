package esi.roadside.assistance.client.main.presentation.screens

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.presentation.components.TopAppBar

@SuppressLint("UnrememberedMutableState")
@Composable
fun RequestAssistance(
    modifier: Modifier = Modifier,
    onSubmitClick: (Field, String) -> Unit,
    navHostController: NavHostController
) {
    var issue by rememberSaveable { mutableStateOf("") }
    var selectedField by rememberSaveable { mutableStateOf<Field?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = R.string.request_assistance,
            drawableRes = R.drawable.union,
            height = 0.34f,
            des = R.string.request_assistance_des
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.selecte_the_field),
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        val fields = listOf(
            Field(R.drawable.construction_24dp_e8eaed_fill0_wght400_grad0_opsz24_1, R.string.mechanic),
            Field(R.drawable.bolt_24dp_e8eaed_fill0_wght400_grad0_opsz24_3, R.string.electrician),
            Field(R.drawable.construction_24dp_e8eaed_fill0_wght400_grad0_opsz24_1, R.string.towing),
            Field(R.drawable.construction_24dp_e8eaed_fill0_wght400_grad0_opsz24_1, R.string.fuel),
            Field(R.drawable.construction_24dp_e8eaed_fill0_wght400_grad0_opsz24_1, R.string.unkown)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            fields.chunked(2).forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowItems.forEach { field ->
                        OutlinedButton(
                            onClick = {
                                selectedField = if (selectedField == field) null else field
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedField == field) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surface,
                                contentColor = if (selectedField == field) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Image(
                                painter = painterResource(field.image),
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = stringResource(field.text))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.des_issue),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = issue,
            onValueChange = { issue = it },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSubmitClick(selectedField ?: fields.last(), issue)
                TODO("Send the field and the des of issue and navigate")
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = stringResource(R.string.submit_req))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRequestAssistance() {
    PreviewAppTheme {
        RequestAssistance(
            onSubmitClick = {
                    _, _ ->
            },
            navHostController = rememberNavController()
        )
    }
}

// 🔹 Field Data Class (No Need for MutableState)
data class Field(
    @DrawableRes val image: Int,
    @StringRes val text: Int
)
