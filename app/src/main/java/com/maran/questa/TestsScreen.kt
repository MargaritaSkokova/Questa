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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maran.questa.navigation.Screen
import com.maran.questa.ui.theme.QuestaTheme

@Composable
fun TestsScreen(
    modifier: Modifier = Modifier,
    tests: List<Test> = listOf(
        Test("Ate comeback",
            "Test your knowledge about Ate comeback of skz",
            "maran",
            "skz",
            true, "Personality"),
        Test(
            "5-star comeback",
            "Test your knowledge about 5-star comeback of skz",
            "maran",
            "skz",
            false, "Personality"
        ),
        Test(
            "Rock-star comeback",
            "Test your knowledge about Rock-star comeback of skz",
            "maran",
            "skz",
            false, "Personality"
        ),
        Test(
            "Noeasy comeback",
            "Test your knowledge about Noeasy comeback of skz",
            "maran",
            "skz",
            false, "Personality"
        ),
        Test(
            "MAXIDENT comeback",
            "Test your knowledge about MAXIDENT comeback of skz",
            "maran",
            "skz",
            false, "Personality"
        ),
        Test(
            "Oddinary comeback",
            "Test your knowledge about Oddinary comeback of skz",
            "maran",
            "skz",
            true, "Personality"
        )
    ),
    navController: NavController
) {
    LazyColumn(
        modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(tests) { item ->
            TestElement(
                test = item,
                navController = navController
            )
        }
    }
}


@Composable
fun TestElement(
    modifier: Modifier = Modifier, test: Test, navController: NavController
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
                if (test.done) {
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
                    modifier = modifier.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(id = R.string.theme) + ": ${test.theme}",
                    modifier = modifier.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
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
                    onClick = { navController.navigate(route = Screen.Choice.route) },
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