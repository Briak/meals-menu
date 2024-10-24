package com.briak.mealsmenu.presentation.common.message

import android.content.Context
import com.briak.mealsmenu.R
import com.briak.mealsmenu.domain.meals.MealNotFoundException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

data class ErrorUiModel(
    private val error: Throwable?,
) : MessageEntity {
    override fun messageText(context: Context): String? = general(context)

    private fun general(context: Context): String? =
        when (error) {
            null -> {
                val format = context.getString(R.string.GENERAL_ERROR_UNEXPECTED)
                format.replace("\$ERROR_MESSAGE\$", "")
            }

            is CancellationException -> null

            is UnknownHostException, is SocketTimeoutException -> context.getString(R.string.GENERAL_ERROR_NO_CONNECTIVITY)

            is MealNotFoundException -> context.getString(R.string.MEAL_DETAILS_NOT_FOUND_ERROR)

            is HttpException -> {
                when (error.code()) {
                    401 -> {
                        null
                    }

                    else -> {
                        val format = context.getString(R.string.GENERAL_ERROR_SERVER)
                        val message = httpMessage(error).orEmpty()
                        format.replace("\$ERROR_MESSAGE\$", message)
                    }
                }
            }

            else -> {
                val format = context.getString(R.string.GENERAL_ERROR_UNEXPECTED)
                format.replace("\$ERROR_MESSAGE\$", error.message ?: "")
            }
        }

    private fun httpMessage(error: HttpException): String? =
        try {
            val body = error.response()?.errorBody()
            if (body != null) {
                val rootObj = JSONObject(body.string())
                rootObj.optString("message") ?: rootObj.optString("Message")
            } else {
                null
            }
        } catch (error: Exception) {
            null
        }
}
