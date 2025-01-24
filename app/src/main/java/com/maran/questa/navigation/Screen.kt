package com.maran.questa.navigation

sealed class Screen(val route: String) {
    object Tests: Screen("tests_screen")
    object Choice: Screen("choice_screen")
    object Login: Screen("login_screen")
}