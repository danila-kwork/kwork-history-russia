package ru.madtwoproject.ui.navigation

sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object Auth: Screen("auth_screen")
    object Questions: Screen("questions_screen")
    object InterestingFact: Screen("interesting_fact_screen")
    object Admin: Screen("admin_screen")
    object Settings: Screen("settings_screen")
    object WithdrawalRequests: Screen("withdrawal_requests_screen")
    object Achievement: Screen("achievement_screen")
    object AddAchievement: Screen("add_achievement_screen")
}