package com.maran.questa.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maran.questa.ChoiceScreen
import com.maran.questa.TestsScreen

@Composable
fun NavigationStack(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Tests.route) {
        composable(route = Screen.Choice.route) {
            ChoiceScreen(modifier = modifier, navController = navController)
        }
        composable(route = Screen.Tests.route) {
            TestsScreen(modifier = modifier, navController = navController)
        }
    }
}