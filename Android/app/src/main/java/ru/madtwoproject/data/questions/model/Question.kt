package ru.madtwoproject.data.questions.model

import com.google.firebase.database.DataSnapshot

data class Question(
    val question: String,
    val answer: String
)

fun DataSnapshot.mapQuestion(): Question {
    return Question(
        question = child("question").value.toString(),
        answer = child("answer").value.toString()
    )
}