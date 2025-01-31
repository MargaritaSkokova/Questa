package com.maran.questa

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.maran.questa.network.models.Model
import com.maran.questa.ui.theme.QuestaTheme
import com.maran.questa.viewModels.TestStatus
import com.maran.questa.viewModels.TestsViewModel

@Composable
fun TestsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    testsViewModel: TestsViewModel = hiltViewModel()
) {
    QuestaTheme {
        Column {
            Text(
                modifier = Modifier
                    .padding(16.dp, 32.dp, 16.dp, 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                text = stringResource(R.string.tests),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            AnimatedContent(
                targetState = testsViewModel.testStatus,
                label = "animated content"
            ) { targetStatus ->
                when (targetStatus) {
                    TestStatus.SUCCESS -> {
                        LazyColumn(
                            Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(testsViewModel.tests) { item ->
                                TestElement(
                                    test = item,
                                    navController = navController
                                )
                            }

                        }
                    }

                    TestStatus.IN_PROCESS -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Loading()
                        }
                    }

                    TestStatus.FAILURE -> {
                        FailureMessage(modifier)
                    }

                    else -> {

                    }
                }
            }
        }
    }
}


@Composable
fun TestElement(
    modifier: Modifier = Modifier, test: Model.Test, navController: NavController
) {
    val show = rememberSaveable {
        mutableStateOf(false)
    }
    QuestaTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .animateContentSize()
                .clickable { show.value = !show.value }
        ) {
            Row(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = test.name,
                    modifier = modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp
                )
                //TODO: room supported value
                if (true) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { show.value = !show.value }) {
                    AnimatedContent(
                        targetState = show.value,
                        transitionSpec = {
                            fadeIn(
                                animationSpec = tween(500)
                            ) togetherWith fadeOut(animationSpec = tween(200))
                        }, label = "Animate arrow"
                    ) { targetState ->
                        Icon(
                            imageVector = if (!targetState)
                                Icons.Rounded.KeyboardArrowDown
                            else
                                Icons.Rounded.KeyboardArrowUp,
                            contentDescription = null,
                            modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        )
                    }
                }
            }
            if (show.value) {
                Text(
                    text = stringResource(id = R.string.author) + ": ${test.author}",
                    modifier = modifier.padding(
                        start = 16.dp,
                        top = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(id = R.string.theme) + ": ${test.theme}",
                    modifier = modifier.padding(
                        start = 16.dp,
                        top = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp
                )
                Text(
                    text = test.description,
                    modifier = modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { navController.navigate(route = Screen.Choice.route + "?testId=${test.id}" + "?testName=${test.name}" + "?isPersonality=${test.testType == "Personality test"}") },
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        stringResource(id = R.string.choose_test_button),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp
                    )
                }
            }

        }
    }
}