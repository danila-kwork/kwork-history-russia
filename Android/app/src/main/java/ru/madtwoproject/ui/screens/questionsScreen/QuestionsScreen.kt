package ru.madtwoproject.ui.screens.questionsScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.joda.time.Period
import ru.madtwoproject.R
import ru.madtwoproject.data.questions.QuestionDataStore
import ru.madtwoproject.data.questions.model.Question
import ru.madtwoproject.data.user.UserDataStore
import ru.madtwoproject.data.user.model.User
import ru.madtwoproject.data.user.model.userSumMoneyVersion2
import ru.madtwoproject.data.utils.UtilsDataStore
import ru.madtwoproject.data.utils.model.Utils
import ru.madtwoproject.ui.theme.primaryText
import ru.madtwoproject.ui.theme.secondaryBackground
import ru.madtwoproject.ui.theme.tintColor
import ru.madtwoproject.ui.view.BaseButton
import ru.madtwoproject.ui.view.Board
import ru.madtwoproject.yandexAds.InterstitialYandexAds

@Composable
fun QuestionsScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val wordsDataStore = remember(::QuestionDataStore)
    var user by remember { mutableStateOf<User?>(null) }
    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    var utils by remember { mutableStateOf<Utils?>(null) }
    var question by remember { mutableStateOf<Question?>(null) }
    var userAnswer by remember { mutableStateOf("") }
    val focusRequester = remember(::FocusRequester)
    var countQuestions by remember { mutableStateOf(0) }

    val interstitialYandexAds = remember {
        InterstitialYandexAds(context, onDismissed = { adClickedDate, returnedToDate ->
            user ?: return@InterstitialYandexAds

            if(adClickedDate != null && returnedToDate != null){
                if((Period(adClickedDate, returnedToDate)).seconds >= 10){
                    userDataStore.updateCountInterstitialAdsClick(user!!.countInterstitialAdsClick + 1)
                }else {
                    userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
                }
            } else {
                userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
            }
        })
    }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
        delay(500L)

        focusRequester.requestFocus()
    })

    LaunchedEffect(key1 = Unit, block = {
        delay(1000L)
        wordsDataStore.getRandomQuestion { question = it; countQuestions++ }
    })

    LaunchedEffect(key1 = countQuestions, block = {
        if(countQuestions % 5 == 0 && countQuestions != 0){
            interstitialYandexAds.show()
        }
    })

    Surface(
        color = Color(0xFF4479af)
    ) {
        Image(
            bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.resources, R.drawable.fonstola),
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

        Column {
            Board(
                modifier = Modifier.padding(10.dp),
                text = userSumMoneyVersion2(
                    utils = utils,
                    countBannerAds = user?.countBannerAds ?: 0,
                    countBannerAdsClick = user?.countBannerAdsClick ?: 0,
                    countInterstitialAds = user?.countInterstitialAds ?: 0,
                    countInterstitialAdsClick = user?.countInterstitialAdsClick ?: 0,
                    countRewardedAds = user?.countRewardedAds ?: 0,
                    countRewardedAdsClick = user?.countRewardedAdsClick ?: 0
                ).toString(),
                width = (screenWidthDp / 2).toDouble(),
                height = (screenHeightDp / 10).toDouble()
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = question?.question ?: "",
                fontWeight = FontWeight.W900,
                modifier = Modifier.padding(5.dp),
                color = primaryText,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                modifier = Modifier
                    .padding(5.dp)
                    .focusRequester(focusRequester),
                value = userAnswer,
                onValueChange = { userAnswer = it },
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = primaryText,
                    disabledTextColor = tintColor,
                    backgroundColor = secondaryBackground,
                    cursorColor = tintColor,
                    focusedBorderColor = tintColor,
                    unfocusedBorderColor = secondaryBackground,
                    disabledBorderColor = secondaryBackground
                ),
                label = {
                    Text(text = "Ответ", color = primaryText)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if(userAnswer.lowercase().trim() == question?.answer?.lowercase()?.trim()){
                        wordsDataStore.getRandomQuestion { question = it; countQuestions++ }
                        userAnswer = ""
                        Toast.makeText(context, "Правильно !", Toast.LENGTH_SHORT).show()
                        user?.countQuestion?.let {
                            userDataStore.updateCountQuestion(it + 1)
                        }
                    }else {
                        Toast.makeText(context, "Не правильно", Toast.LENGTH_SHORT).show()
                    }
                })
            )

            BaseButton(
                text = "Подсказка",
                onClick = {
                    userAnswer = question?.answer.toString()
                }
            )
        }
    }
}