package com.maran.questa

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.maran.questa.navigation.Screen
import com.maran.questa.ui.theme.QuestaTheme
import com.maran.questa.viewModels.LoginStatus
import com.maran.questa.viewModels.LoginViewModel
import com.maran.questa.viewModels.PasswordError
import com.maran.questa.viewModels.VerificationError
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    var showButtons by remember { mutableStateOf(true) }
    val showSignIn = remember { mutableStateOf(false) }
    val showSignUp = remember { mutableStateOf(false) }
    val (passwordVisible, setPasswordVisible) = remember { mutableStateOf(false) }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        loginViewModel.check()
    }

    BackHandler(enabled = !showButtons) {
        showButtons = true
        showSignUp.value = false
        showSignIn.value = false
    }

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
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(id = R.string.moto),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            AnimatedContent(
                targetState = loginViewModel.loginStatus,
                label = "animated content"
            ) { targetStatus ->
                when (targetStatus) {
                    LoginStatus.NOT_LOGGED -> {
                        if (showButtons) {
                            Column(
                                modifier = Modifier
                                    .wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = {
                                        showButtons = false; showSignIn.value = true
                                    },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .wrapContentSize()
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.sign_in),
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                                Button(
                                    onClick = {
                                        showButtons = false; showSignUp.value = true
                                    },
                                    modifier = Modifier
                                        .wrapContentSize()
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.sign_up),
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
                        }
                        if (showSignIn.value || showSignUp.value) {
                            LoginFields(
                                modifier,
                                loginViewModel,
                                showSignIn,
                                passwordVisible,
                                setPasswordVisible
                            )
                        }
                    }

                    LoginStatus.IN_PROCESS -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(64.dp)
                                .padding(8.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }

                    LoginStatus.SUCCESS -> {
                        navController.navigate(route = Screen.Tests.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .wrapContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            LoginFields(
                                modifier,
                                loginViewModel,
                                showSignIn,
                                passwordVisible,
                                setPasswordVisible
                            )
                            Text(
                                text = stringResource(R.string.error_login),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = loginViewModel.loginMessage,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginFields(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    showSignIn: MutableState<Boolean>,
    passwordVisible: Boolean,
    setPasswordVisible: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            value = loginViewModel.username,
            onValueChange = { username ->
                loginViewModel.updateUsername(username); loginViewModel.validateUsername(
                username
            )
            },
            maxLines = 1,
            label = { Text(stringResource(R.string.login)) },
            supportingText = {
                Row {
                    Text(
                        if (loginViewModel.isErrorUsername) stringResource(R.string.text_field_username_empty) else "",
                        Modifier.clearAndSetSemantics {})
                    Spacer(Modifier.weight(1f))
                }
            },
            isError = loginViewModel.isErrorUsername,
        )

        TextField(
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            value = loginViewModel.password,
            onValueChange = { password ->
                loginViewModel.updatePassword(password); loginViewModel.validatePassword(
                password, showSignIn.value
            )
            },
            label = { Text(stringResource(R.string.password)) },
            maxLines = 1,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image =
                    if (passwordVisible) painterResource(R.drawable.eye_open) else painterResource(
                        R.drawable.eye_closed
                    )
                val description =
                    if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { setPasswordVisible(!passwordVisible) }) {
                    Image(image, contentDescription = description)
                }
            },
            supportingText = {
                Row {
                    Text(
                        if (loginViewModel.isErrorPassword) {
                            when (loginViewModel.getPasswordError()) {
                                PasswordError.EMPTY -> stringResource(R.string.text_field_password_empty)
                                PasswordError.NOT_MATCH -> stringResource(R.string.text_field_password_not_match)
                                else -> ""
                            }
                        } else {
                            ""
                        },
                        Modifier.clearAndSetSemantics {})
                    Spacer(Modifier.weight(1f))
                }
            },
            isError = loginViewModel.isErrorPassword,

            )

        if (showSignIn.value) {
            ButtonSignInSignUp(
                isEnabled = !loginViewModel.isErrorUsername &&
                        !loginViewModel.isErrorPassword &&
                        loginViewModel.username.isNotEmpty() &&
                        loginViewModel.password.isNotEmpty(),
                action = { loginViewModel.login() }, text = stringResource(id = R.string.sign_in)
            )
        } else {
            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                value = loginViewModel.passwordVerification,
                onValueChange = { password ->
                    loginViewModel.updatePasswordVerification(password);
                    loginViewModel.validatePasswordSignUp(password, loginViewModel.password)
                },
                label = { Text(stringResource(R.string.password_verification)) },
                maxLines = 1,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                supportingText = {
                    Row {
                        Text(
                            if (loginViewModel.isErrorPasswordVerification) {
                                when (loginViewModel.getVerificationError()) {
                                    VerificationError.EMPTY -> stringResource(R.string.text_field_password_verification_empty)
                                    VerificationError.DIFFERENT -> stringResource(R.string.text_field_password_verification_different)
                                    else -> ""
                                }
                            } else {
                                ""
                            },
                            Modifier.clearAndSetSemantics {})
                        Spacer(Modifier.weight(1f))
                    }
                },
                isError = loginViewModel.isErrorPasswordVerification,
            )
            ButtonSignInSignUp(
                isEnabled = !loginViewModel.isErrorUsername &&
                        !loginViewModel.isErrorPassword && !loginViewModel.isErrorPasswordVerification &&
                        loginViewModel.username.isNotEmpty() &&
                        loginViewModel.password.isNotEmpty() &&
                        loginViewModel.passwordVerification.isNotEmpty(),
                action = { loginViewModel.signUp() }, text = stringResource(id = R.string.sign_up)
            )
        }
    }
}

@Composable
fun ButtonSignInSignUp(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    action: () -> Unit,
    text: String
) {
    Button(
        onClick = action,
        enabled = isEnabled,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
