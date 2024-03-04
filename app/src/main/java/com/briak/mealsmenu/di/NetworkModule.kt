package com.briak.mealsmenu.di

import com.briak.mealsmenu.BuildConfig
import com.briak.mealsmenu.data.network.MealsAPI
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object NetworkModule {

    private const val CONTENT_HEADER_KEY = "Content-Type"
    private const val CONTENT_HEADER_VALUE = "application/json"

    private const val OK_HTTP_TAG = "OkHttp"

    private val LOGGING_LEVEL = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }

    val module = module {

        single {
            HttpLoggingInterceptor { message ->
                Timber.tag(OK_HTTP_TAG).d(message)
            }.apply {
                level = LOGGING_LEVEL
            }
        }

        single {
            Interceptor { chain ->
                val request = chain.request()
                val requestBuilder = request
                    .newBuilder()
                    .addHeader(CONTENT_HEADER_KEY, CONTENT_HEADER_VALUE)
                    .build()
                chain.proceed(requestBuilder)
            }
        }

        single {
            val builder = OkHttpClient.Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .addInterceptor(get<Interceptor>())
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(OkHttpProfilerInterceptor())
                    }
                }
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
            builder.build()
        }

        single {
            Retrofit.Builder()
                .client(get())
                .addConverterFactory(GsonConverterFactory.create(get()))
                .baseUrl(BuildConfig.SERVER_URL)
                .build()
                .create(MealsAPI::class.java)
        }
    }

}