package com.maran.questa

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maran.questa.navigation.Screen
import com.maran.questa.ui.theme.QuestaTheme
import com.maran.questa.viewModels.ResultState
import com.maran.questa.viewModels.ResultsViewModel
import java.util.UUID

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    testId: String,
    testName: String,
    isPersonality: Boolean,
    personality: String?,
    score: Int?,
    navController: NavController,
    resultsViewModel: ResultsViewModel = hiltViewModel()
) {
    resultsViewModel.getResult(UUID.fromString(testId), isPersonality, personality, score)

    QuestaTheme {
        AnimatedContent(
            targetState = resultsViewModel.resultState,
            label = "animated content"
        ) { targetStatus ->
            when (targetStatus) {
                ResultState.SUCCESS -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        TestName(testName = testName)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                                text = stringResource(R.string.result)
                            )
                            Text(
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 36.sp,
                                text = if (isPersonality) {
                                    personality!!
                                } else {
                                    score.toString()
                                }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(24.dp)
                        ) {
                            Column(
                                Modifier
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontSize = 20.sp,
                                    lineHeight = 32.sp,
                                    textAlign = TextAlign.Center,
                                    text = resultsViewModel.resultDescription
                                )
                            }
                        }
                        Buttons(
                            {
                                navController.navigate(route = Screen.Choice.route + "?testId=${testId}" + "?testName=${testName}" + "?isPersonality=${isPersonality}") {
                                    popUpTo(Screen.Tests.route) { inclusive = true }
                                }
                            },
                            {
                                navController.navigate(route = Screen.Tests.route) {
                                    popUpTo(Screen.Tests.route) { inclusive = true }
                                }
                            })
                    }
                }

                ResultState.FAILURE -> {
                    FailureMessage(modifier)
                }

                ResultState.NOT_FOUND -> {
                    NotFound()
                    Buttons(
                        {
                            navController.navigate(route = Screen.Choice.route + "?testId=${testId}" + "?testName=${testName}" + "?isPersonality=${isPersonality}") {
                                popUpTo(Screen.Tests.route) { inclusive = true }
                            }
                        },
                        {
                            navController.navigate(route = Screen.Tests.route) {
                                popUpTo(Screen.Tests.route) { inclusive = true }
                            }
                        })
                }

                ResultState.IN_PROCESS -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Loading()
                    }
                }
            }
        }
    }
}

@Composable
fun TestName(modifier: Modifier = Modifier, testName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = testName
        )
    }
}

@Composable
fun Buttons(retakeAction: () -> Unit, toTestAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { retakeAction() },
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(
                stringResource(id = R.string.retake),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 20.sp
            )
        }
        Button(
            onClick = { toTestAction() },
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(
                stringResource(id = R.string.back_to_tests),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun NotFound(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = modifier
                .padding(8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            text = stringResource(R.string.not_found_result)
        )
    }
}