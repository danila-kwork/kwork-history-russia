package ru.madtwoproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.madtwoproject.ui.navigation.Screen
import ru.madtwoproject.ui.screens.achievementScreen.AchievementScreen
import ru.madtwoproject.ui.screens.addAchievementScreen.AddAchievementScreen
import ru.madtwoproject.ui.screens.adminScreen.AdminScreen
import ru.madtwoproject.ui.screens.authScreen.AuthScreen
import ru.madtwoproject.ui.screens.interestingFactScreen.InterestingFactScreen
import ru.madtwoproject.ui.screens.mainScreen.MainScreen
import ru.madtwoproject.ui.screens.questionsScreen.QuestionsScreen
import ru.madtwoproject.ui.screens.settingsScreen.SettingsScreen
import ru.madtwoproject.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import ru.madtwoproject.ui.theme.HistoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoryTheme {
                val navController = rememberNavController()
                val auth = remember(Firebase::auth)

                NavHost(
                    navController = navController,
                    startDestination = if(auth.currentUser == null)
                        Screen.Auth.route
                    else
                        Screen.Main.route
                ){
                    composable(Screen.Auth.route){
                        AuthScreen(navController = navController)
                    }
                    composable(Screen.Main.route){
                        MainScreen(navController = navController)
                    }
                    composable(Screen.Admin.route){
                        AdminScreen(navController = navController)
                    }
                    composable(Screen.Settings.route){
                        SettingsScreen(navController = navController)
                    }
                    composable(Screen.WithdrawalRequests.route){
                        WithdrawalRequestsScreen(navController = navController)
                    }
                    composable(Screen.Questions.route){
                        QuestionsScreen(navController = navController)
                    }
                    composable(Screen.InterestingFact.route){
                        InterestingFactScreen(navController = navController)
                    }
                    composable(Screen.Achievement.route){
                        AchievementScreen(navController = navController)
                    }
                    composable(Screen.AddAchievement.route){
                        AddAchievementScreen(navController = navController)
                    }
                }
            }
        }
    }
}