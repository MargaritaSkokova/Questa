package com.maran.questa.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maran.questa.ChoiceScreen
import com.maran.questa.LoginScreen
import com.maran.questa.ResultScreen
import com.maran.questa.TestsScreen

@Composable
fun NavigationStack(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Choice.route + "?testId={testId}" + "?testName={testName}" + "?isPersonality={isPersonality}",
            arguments = listOf(
                navArgument("testId") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("testName") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("isPersonality") {
                    type = NavType.BoolType
                    nullable = false
                }
            )) {
            ChoiceScreen(
                modifier = modifier,
                navController = navController,
                testId = it.arguments?.getString("testId")!!,
                testName = it.arguments?.getString("testName")!!,
                isPersonality = it.arguments?.getBoolean("isPersonality")!!
            )
        }
        composable(route = Screen.Tests.route) {
            TestsScreen(modifier = modifier, navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(modifier = modifier, navController = navController)
        }
        composable(route = Screen.Result.route + "?testId={testId}" + "?testName={testName}" + "?isPersonality={isPersonality}" + "?personality={personality}" + "?score={score}",
            arguments = listOf(
                navArgument("testId") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("testName") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("isPersonality") {
                    type = NavType.BoolType
                    nullable = false
                },
                navArgument("personality") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("score") {
                    type = NavType.IntType
                    nullable = true
                }
            )) {

            ResultScreen(
                modifier = modifier, navController = navController,
                testId = it.arguments?.getString("testId")!!,
                testName = it.arguments?.getString("testName")!!,
                isPersonality = it.arguments?.getBoolean("isPersonality")!!,
                personality = it.arguments?.getString("personality"),
                score = it.arguments?.getInt("score")
            )
        }
    }
}