package com.android.aniviewer_kun.api

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class AniListApi {
    private val url = "https://graphql.anilist.co"

    fun getApolloClient(): ApolloClient {
        val tokenInterceptor = TokenInterceptor()
        val okHttpClient = if (AniListUser.token != null) OkHttpClient.Builder().addInterceptor(tokenInterceptor).build() else OkHttpClient.Builder().build()

        return ApolloClient.Builder().okHttpClient(okHttpClient).serverUrl(url).build()
    }
}

class TokenInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer " + AniListUser.token)
            .build()

        return chain.proceed(request)
    }
}