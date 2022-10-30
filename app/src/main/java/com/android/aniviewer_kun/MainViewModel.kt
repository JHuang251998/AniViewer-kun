package com.android.aniviewer_kun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aniviewer_kun.api.AniListApi
import com.android.aniviewer_kun.api.Repository
import com.android.aniviewer_kun.type.MediaStatus
import com.android.aniviewer_kun.type.MediaType
import com.android.aniviewer_kun.type.MediaSort
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val api = AniListApi()
    private val repository = Repository(api)
    var fetchDone : MutableLiveData<Boolean> = MutableLiveData(false)

    private var media = MutableLiveData<List<MediaListQuery.Medium?>?>()

    fun getMediaListByStatus(status: Optional<MediaStatus?>, type: MediaType, sort: List<MediaSort>) {
        viewModelScope.launch(
        context = viewModelScope.coroutineContext
            + Dispatchers.IO) {
            try {
                val page = repository.getMediaByStatus(status, type, sort)
                media.postValue(page?.media)
            } catch (e: ApolloException) {
                println("Error fetching media: $e")
            }
        }
    }

    fun observeMedia(): LiveData<List<MediaListQuery.Medium?>?> {
        return media
    }
}