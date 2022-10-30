package com.android.aniviewer_kun.api

import com.android.aniviewer_kun.MediaListQuery
import com.android.aniviewer_kun.type.MediaStatus
import com.android.aniviewer_kun.type.MediaType
import com.android.aniviewer_kun.type.MediaSort
import com.apollographql.apollo3.api.Optional

class Repository(private val api: AniListApi) {
    suspend fun getMediaByStatus(status: Optional<MediaStatus?>, type: MediaType, sort: List<MediaSort>): MediaListQuery.Page? {
        val response = api.getApolloClient().query(MediaListQuery(status, type, sort)).execute()
        return response.data?.Page
    }
}