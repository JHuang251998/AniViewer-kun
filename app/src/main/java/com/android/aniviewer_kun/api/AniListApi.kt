package com.android.aniviewer_kun.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient

class AniListApi {
    private val url = "https://graphql.anilist.co/"
    fun getApolloClient(): ApolloClient {
        val okHttpClient = OkHttpClient.Builder().build()
        return ApolloClient.Builder().serverUrl(url).okHttpClient(okHttpClient).build()
    }
}