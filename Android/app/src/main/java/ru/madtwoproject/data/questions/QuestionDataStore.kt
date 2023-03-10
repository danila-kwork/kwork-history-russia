package ru.madtwoproject.data.questions

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.madtwoproject.data.questions.model.Question
import ru.madtwoproject.data.questions.model.mapQuestion

class QuestionDataStore {

    private val db = Firebase.database

    fun getRandomQuestion(
        onSuccess: (Question) -> Int
    ) {
        val randomId = (0..10).random()

        db.reference.child("questions").child(randomId.toString()).get()
            .addOnSuccessListener { onSuccess(it.mapQuestion()) }
    }
}