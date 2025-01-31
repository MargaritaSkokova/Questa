package com.maran.questa

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maran.questa.network.models.Model
import com.maran.questa.ui.theme.QuestaTheme
import com.maran.questa.viewModels.ChoiceViewModel
import com.maran.questa.viewModels.QuestionStatus.FAILURE
import com.maran.questa.viewModels.QuestionStatus.IN_PROCESS
import com.maran.questa.viewModels.QuestionStatus.SUCCESS
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ChoiceScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    testId: String?,
    testName: String?,
    isPersonality: Boolean?,
    choiceViewModel: ChoiceViewModel = hiltViewModel()
) {
    choiceViewModel.initialize(UUID.fromString(testId), isPersonality!!)
    val coroutineScope = rememberCoroutineScope()
    var close by remember {
        mutableStateOf(false)
    }
    var leave by remember {
        mutableStateOf(false)
    }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressHandled by remember { mutableStateOf(false) }
    BackHandler(enabled = !backPressHandled) {
        if (leave) {
            backPressHandled = true
            coroutineScope.launch {
                awaitFrame()
                onBackPressedDispatcher?.onBackPressed()
                leave = false
                close = false
                backPressHandled = false
            }
        }
        close = !close
    }

    QuestaTheme {
        AnimatedContent(
            targetState = choiceViewModel.questionStatus,
            label = "animated content"
        ) { targetStatus ->
            when (targetStatus) {
                SUCCESS -> {
                    Column(modifier = modifier.fillMaxHeight()) {
                        Row(horizontalArrangement = Arrangement.Start) {
                            Row(Modifier.weight(1f)) {
                                IconButton(onClick = { close = true }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = null
                                    )
                                }
                            }
                            Text(
                                modifier = modifier
                                    .padding(8.dp),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                text = testName ?: ""
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        QuestionElement(text = choiceViewModel.currQuestion.value?.text ?: "")
                        ChoiceList(
                            choices = choiceViewModel.currAnswers.value,
                            stateIndex = choiceViewModel.currChosen,
                            show = choiceViewModel.show,
                            modifier = Modifier.weight(1f),
                            isDisabled = close
                        )
                        BottomPanel(
                            modifier,
                            choiceViewModel::getPrevious,
                            choiceViewModel::getNext,
                            close,
                            choiceViewModel.isPrevious,
                            choiceViewModel.currNumber,
                            choiceViewModel.numberQuestions,
                            choiceViewModel.show
                        )

                    }

                }

                IN_PROCESS -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = modifier
                                .padding(8.dp),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = testName ?: ""
                        )
                        Loading()
                    }
                }

                FAILURE -> {
                    FailureMessage(modifier)
                }
            }
        }
        AnimatedVisibility(
            visible = close,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(horizontal = 10.dp)
                        .width(250.dp)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .align(Alignment.Center)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = R.string.close_message),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer

                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly, modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Button(
                            onClick = { leave = true; onBackPressedDispatcher?.onBackPressed() },
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Text(text = stringResource(id = R.string.yes))
                        }
                        Button(onClick = { close = false }, modifier = Modifier.wrapContentSize()) {
                            Text(text = stringResource(id = R.string.no))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FailureMessage(modifier: Modifier = Modifier) {
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
            text = stringResource(R.string.error)
        )
    }
}

@Composable
fun Loading() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(64.dp)
            .padding(8.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    backRowAction: () -> Unit,
    nextRowAction: () -> Unit,
    close: Boolean,
    isPrevious: MutableState<Boolean>,
    currNumber: MutableIntState,
    numberQuestions: Int,
    show: MutableState<Boolean>,

    ) {
    Row(horizontalArrangement = Arrangement.Start) {
        Row(Modifier.weight(1f)) {
            IconButton(
                onClick = { backRowAction() },
                enabled = !close && isPrevious.value
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
        AnimatedContent(
            targetState = currNumber,
            label = "animated content"
        ) { targetNumber ->
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = "${targetNumber.intValue} of $numberQuestions",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
        AnimatedContent(
            targetState = show.value,
            label = "animated content",
            modifier = Modifier.weight(1f)
        ) { targetValue ->
            when (targetValue) {
                true -> {
                    Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = { nextRowAction() }, enabled = !close) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }
                }

                false -> {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun QuestionElement(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            fontSize = 28.sp
        )
    }
}


@Composable
fun ChoiceElement(
    modifier: Modifier = Modifier,
    text: String,
    selected: MutableState<Int>,
    index: Int,
    show: MutableState<Boolean>,
    isDisabled: Boolean
) {
    QuestaTheme {
        Row(
            modifier = modifier
                .padding(4.dp)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .clickable(enabled = !isDisabled) {
                    selected.value = index
                    show.value = true
                }
        ) {
            RadioButton(
                selected = (selected.value == index),
                enabled = !isDisabled,
                onClick = {
                    selected.value = index
                    show.value = true
                },
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .padding(4.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.tertiary,
                    unselectedColor = MaterialTheme.colorScheme.secondary
                )
            )
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ChoiceList(
    modifier: Modifier = Modifier,
    choices: List<Model.Answer>,
    show: MutableState<Boolean>,
    isDisabled: Boolean,
    stateIndex: MutableIntState
) {
    val state = remember {
        stateIndex
    }
    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            userScrollEnabled = !isDisabled
        ) {
            itemsIndexed(choices) { index, item ->
                ChoiceElement(
                    text = item.text,
                    selected = state,
                    index = index,
                    show = show,
                    isDisabled = isDisabled
                )
            }
        }
    }
}
