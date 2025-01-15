package com.maran.questa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maran.questa.ui.theme.QuestaTheme

@Composable
@Preview
fun LoginScreen(modifier: Modifier = Modifier) {
    val showButtons = remember { mutableStateOf(true) }
    val showSignIn = remember { mutableStateOf(false) }
    val showSignUp = remember { mutableStateOf(false) }
    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    QuestaTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(id = R.string.moto),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (showButtons.value) {
                Button(
                    onClick = { showButtons.value = false; showSignIn.value = true },
                    modifier = modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                        .wrapContentHeight()
                        .width(120.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Button(
                    onClick = { showButtons.value = false; showSignUp.value = true },
                    modifier = modifier
                        .wrapContentHeight()
                        .width(120.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            if (showSignIn.value || showSignUp.value) {
                TextField(
                    modifier = modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    value = login.value,
                    onValueChange = { login.value = it },
                    maxLines = 1,
                    label = { Text("Username")}
                )

                TextField(
                    modifier = modifier.clip(RoundedCornerShape(8.dp)),
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password")},
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                
                if (showSignIn.value) {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = modifier
                            .padding(8.dp)
                            .wrapContentHeight()
                            .width(120.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_in),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                } else {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = modifier
                            .padding(8.dp)
                            .wrapContentHeight()
                            .width(120.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_up),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}