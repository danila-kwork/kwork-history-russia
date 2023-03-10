package ru.madtwoproject.ui.screens.achievementScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ru.madtwoproject.R
import ru.madtwoproject.data.achievement.AchievementDataStore
import ru.madtwoproject.data.achievement.model.Achievement
import ru.madtwoproject.data.achievement.model.AchievementType
import ru.madtwoproject.data.user.UserDataStore
import ru.madtwoproject.data.user.model.User
import ru.madtwoproject.ui.theme.primaryText
import ru.madtwoproject.ui.theme.secondaryBackground
import ru.madtwoproject.ui.theme.tintColor

@Composable
fun AchievementScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    var achievementList by remember { mutableStateOf(emptyList<Achievement>()) }
    val achievementDataStore = remember(::AchievementDataStore)
    val userDataStore = remember(::UserDataStore)
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        delay(1000L)
        achievementDataStore.getList {
            achievementList = it.filter {
                it.id !in (user?.achievementIds ?: emptyList())
            }
        }
    })

    Image(
        bitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.main_background),
            screenWidthDp,
            screenHeightDp,
            false
        ).asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.size(
            width = screenWidthDp.dp,
            height = screenHeightDp.dp
        )
    )

    if(user != null){
        LazyColumn {
            items(achievementList){ item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 15.dp,
                            vertical = 5.dp
                        ),
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    backgroundColor = secondaryBackground
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            when(item.type){
                                AchievementType.QUESTIONS -> {
                                    Text(
                                        text = "Ответьте на вопросы",
                                        color = primaryText,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                                AchievementType.INTERESTING_FACT -> {
                                    Text(
                                        text = "Посмотрите интересные факты",
                                        color = primaryText,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                            }

                            Text(
                                text = "${if(item.type == AchievementType.QUESTIONS)
                                    user!!.countQuestion
                                else
                                    user!!.countInterestingFact}/${item.count}",
                                color = primaryText,
                                modifier = Modifier.padding(5.dp)
                            )
                        }

                        Text(
                            text = "Награда ${item.reward} рублей",
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        val isReward = when(item.type){
                            AchievementType.QUESTIONS -> user!!.countQuestion >= item.count
                            AchievementType.INTERESTING_FACT -> user!!.countInterestingFact >= item.count
                        }

                        AnimatedVisibility(visible = isReward) {
                            Button(
                                modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                shape = AbsoluteRoundedCornerShape(15.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = tintColor
                                ),
                                onClick = { /*TODO*/ }
                            ) {
                                Text(text = "Забрать награду")
                            }
                        }
                    }
                }
            }
        }
    }
}