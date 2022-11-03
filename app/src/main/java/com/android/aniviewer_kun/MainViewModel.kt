package com.android.aniviewer_kun

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aniviewer_kun.api.AniListApi
import com.android.aniviewer_kun.api.Repository
import com.android.aniviewer_kun.type.MediaStatus
import com.android.aniviewer_kun.type.MediaType
import com.android.aniviewer_kun.type.MediaSort
import com.android.aniviewer_kun.ui.AnimeDetails
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

    fun doAnimeDetails(context: Context, anime: MediaListQuery.Medium?) {
        val startDate = "${anime?.startDate?.month} / ${anime?.startDate?.day} / ${anime?.startDate?.year}"
        val endDate = if (anime?.endDate?.month == null) {
            "-"
        } else {
            "${anime.endDate.month} / ${anime.endDate.day} / ${anime.endDate.year}"
        }
        val otherNames = anime?.synonyms?.joinToString(", ")
        val genres = anime?.genres?.joinToString(", ")

        val intent = Intent(context, AnimeDetails::class.java)
        if (anime != null) {
            intent.putExtra(AnimeDetails.animeTitleRomajiKey, anime.title?.romaji)
            intent.putExtra(AnimeDetails.animePicKey, anime.coverImage?.medium)
            intent.putExtra(AnimeDetails.animeAverageScore, anime.averageScore)
            intent.putExtra(AnimeDetails.animeFavorites, anime.favourites)
            intent.putExtra(AnimeDetails.animePopularity, anime.popularity)
            intent.putExtra(AnimeDetails.animeDescription, anime.description)
            intent.putExtra(AnimeDetails.animeFormat, anime.type?.name)
            intent.putExtra(AnimeDetails.animeStartDate, startDate)
            intent.putExtra(AnimeDetails.animeEndDate, endDate)
            intent.putExtra(AnimeDetails.animeOtherNames, otherNames)
            intent.putExtra(AnimeDetails.animeCountryOfOrigin, anime.countryOfOrigin?.name)
            intent.putExtra(AnimeDetails.animeGenres, genres)
        }
        context.startActivity(intent)
    }
}