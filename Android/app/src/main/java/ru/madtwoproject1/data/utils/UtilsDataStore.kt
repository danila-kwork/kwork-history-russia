package ru.madtwoproject1.data.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.madtwoproject1.data.utils.model.Utils
import ru.madtwoproject1.data.utils.model.mapUtils

class UtilsDataStore {

    private val database = Firebase.database

    fun get(onSuccess: (Utils) -> Unit, onError:(message:String) -> Unit = {}) {

        database.reference.child("utils").get()
            .addOnSuccessListener {
                onSuccess(it.mapUtils())
            }
            .addOnFailureListener {
                onError(it.message ?: "Ошибка")
            }
    }

    fun getWordsCount(onSuccess: (Int) -> Unit) {

        database.reference.child("utils").child("words_count").get()
            .addOnSuccessListener { onSuccess(it.value.toString().toInt()) }
    }

    fun updateReferralLink(referralLink:Boolean, onSuccess: () -> Unit) {

        database.reference.child("utils").child("referral_link").setValue(referralLink)
            .addOnSuccessListener { onSuccess() }
    }

    fun create(utils: Utils, onSuccess: () -> Unit) {
        database.reference.child("utils").setValue(utils)
            .addOnSuccessListener { onSuccess() }
    }
}