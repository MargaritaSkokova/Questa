package com.maran.questa.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private var gson = GsonBuilder()
            .setLenient()
            .create()

        fun retrofitClient(apiKey: String): Retrofit = Retrofit.Builder()
            .baseUrl("http://x.x.x.x:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(okHttpClient(apiKey).build())
            .build()

        fun retrofitClientAuthentication(): Retrofit = Retrofit.Builder()
            .baseUrl("http://x.x.x.x:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()

        private fun okHttpClient(apiKey: String) = OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .header("accept", "*/*")
                    .header("Authorization", "Bearer $apiKey")
                    .build()
                chain.proceed(request)
            }
    }
}