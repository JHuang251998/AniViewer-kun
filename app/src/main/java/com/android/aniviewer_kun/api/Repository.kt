package com.android.aniviewer_kun.api

import android.util.Log
import com.android.aniviewer_kun.MediaListQuery
import com.android.aniviewer_kun.ViewerQuery
import com.android.aniviewer_kun.type.MediaStatus
import com.android.aniviewer_kun.type.MediaType
import com.android.aniviewer_kun.type.MediaSort
import com.apollographql.apollo3.api.BooleanExpression
import com.apollographql.apollo3.api.Optional

class Repository(private val api: AniListApi) {
    suspend fun getMediaByStatus(status: Optional<MediaStatus?>, type: MediaType, sort: List<MediaSort>): MediaListQuery.Page? {
        val response = api.getApolloClient().query(MediaListQuery(status, type, sort)).execute()
        return response.data?.Page
    }

    suspend fun getViewerData(): ViewerQuery.Viewer? {
        val response = api.getApolloClient().query(ViewerQuery()).execute()
        val user = response?.data?.Viewer

        Log.d("XXX", "${user?.name}")
        Log.d("XXX", "${user?.id}")
        Log.d("XXX", "${user?.avatar}")
        AniListUser.userName = user?.name
        AniListUser.userID = user?.id
        AniListUser.avatar = user?.avatar?.medium

        return response.data?.Viewer
    }
}