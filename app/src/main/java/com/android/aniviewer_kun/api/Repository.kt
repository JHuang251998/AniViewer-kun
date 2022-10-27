package com.android.aniviewer_kun.api

import com.android.aniviewer_kun.AnimeListQuery

class Repository(private val api: AniListApi) {
    suspend fun getMedia() {
        val response = api.getApolloClient().query(AnimeListQuery()).execute()
        println("RESPONSE: ${response.data}")
    }
}