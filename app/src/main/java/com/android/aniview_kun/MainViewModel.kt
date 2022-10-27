package com.android.aniview_kun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aniview_kun.api.AniListApi
import com.android.aniview_kun.api.Repository
import com.apollographql.apollo3.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val api = AniListApi()
    private val repository = Repository(api)

    fun getMedia() {
        viewModelScope.launch(
        context = viewModelScope.coroutineContext
            + Dispatchers.IO) {
            try {
                repository.getMedia()
            } catch (e: ApolloException) {
                println("Error fetching media")
            }
        }
    }
}