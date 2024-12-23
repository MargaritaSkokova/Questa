package com.maran.questa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maran.questa.ui.theme.QuestaTheme

@Preview
@Composable
fun ChoiceScreen(
    modifier: Modifier = Modifier,
    question: String = "What is the first word in the chorus of `ChkChk Boom`?",
    choices: List<String> = listOf(
        "ChkChk",
        "Boom",
        "vamos",
        "lobos",
        "chaos",
        "Boom",
        "vamos",
        "lobos",
        "chaos",
        "Boom",
        "vamos",
        "lobos",
        "chaos"
    ),
    numberQuestion: Int = 10,
    currQuestion: Int = 0,
    name: String = "Name"
) {
    val show = remember {
        mutableStateOf(false)
    }
    var close by remember {
        mutableStateOf(false)
    }
    QuestaTheme {
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
                    text = name
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            QuestionElement(text = question)
            ChoiceList(choices = choices, show = show, modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.Start) {
                Row(Modifier.weight(1f)) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = "$currQuestion of $numberQuestion",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                if (!show.value) {
                    Spacer(modifier = Modifier.weight(1f))
                }
                AnimatedVisibility(
                    visible = show.value,
                    modifier = Modifier.weight(1f),
                    enter = scaleIn()
                ) {
                    Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = close,
            enter = fadeIn(),
            exit = fadeOut()
        )  {
            Box(modifier = modifier.fillMaxSize()) {
                Column(
                    modifier = modifier
                        .padding(4.dp)
                        .padding(horizontal = 10.dp)
                        .width(250.dp)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .align(Alignment.Center)
                ) {
                    Text(
                        modifier = modifier.padding(8.dp),
                        text = stringResource(id = R.string.close_message),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer

                    )
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = modifier
                        .fillMaxWidth()
                        .height(30.dp)) {
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.yes))
                        }
                        Button(onClick = { close = false }) {
                            Text(text = stringResource(id = R.string.no))
                        }
                    }
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
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
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
    show: MutableState<Boolean>
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
                .clickable {
                    selected.value = index
                    show.value = true
                }
        ) {
            RadioButton(
                selected = (selected.value == index),
                onClick = {
                    selected.value = index
                    show.value = true
                },
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.tertiary,
                    unselectedColor = MaterialTheme.colorScheme.secondary
                )
            )
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.align(Alignment.CenterVertically),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ChoiceList(
    modifier: Modifier = Modifier,
    choices: List<String>,
    show: MutableState<Boolean>
) {
    val state = remember {
        mutableStateOf(-1)
    }
    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            itemsIndexed(choices) { index, item ->
                ChoiceElement(text = item, selected = state, index = index, show = show)
            }
        }
    }
}
