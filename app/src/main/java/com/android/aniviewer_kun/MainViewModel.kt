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
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val api = AniListApi()
    private val repository = Repository(api)
    var fetchDone: MutableLiveData<Boolean> = MutableLiveData(false)

    private var media = MutableLiveData<List<MediaListQuery.Medium?>?>()
    private var viewerData = MutableLiveData<ViewerQuery.Viewer?>()
    private var searchMediaResults = MutableLiveData<List<MediaListQuery.Medium?>?>()

    fun getMediaListByStatus(status: MediaStatus, type: MediaType, sort: List<MediaSort>, currentPage: Int, perPage: Int) {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO
        ) {
            try {
                val page = repository.getMediaList(
                    Optional.present(status),
                    Optional.present(type),
                    Optional.present(sort),
                    Optional.present(currentPage),
                    Optional.present(perPage),
                    Optional.absent()
                )
                media.postValue(page?.media)
            } catch (e: ApolloException) {
                println("Error fetching media: $e")
            }
        }
    }

    fun searchMedia(search: String, perPage: Int) {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO) {
            try {
                val page = repository.getMediaList(
                    Optional.absent(),
                    Optional.Present(MediaType.ANIME),
                    Optional.present(listOf(MediaSort.POPULARITY_DESC)),
                    Optional.absent(),
                    Optional.present(perPage),
                    Optional.present(search)
                )
                searchMediaResults.postValue(page?.media)
            } catch (e: ApolloException) {
                println("Error fetching media: $e")
            }
        }
    }

    fun getTop100Media(type: MediaType, sort:  List<MediaSort>) {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO) {
            try {
                val mediaList = repository.getMediaList(
                    Optional.absent(),
                    Optional.present(type),
                    Optional.present(sort),
                    Optional.present(1),
                    Optional.absent(),
                    Optional.absent()
                )?.media?.toMutableList()
                val page2Media = repository.getMediaList(
                    Optional.absent(),
                    Optional.present(type),
                    Optional.present(sort),
                    Optional.present(2),
                    Optional.absent(),
                    Optional.absent()
                )?.media
                if (page2Media != null) {
                    mediaList?.addAll(page2Media)
                }
                media.postValue(mediaList)
            } catch (e: ApolloException) {
                println("Error fetching top 100: $e")
            }
        }
    }

    fun observeMedia(): LiveData<List<MediaListQuery.Medium?>?> {
        return media
    }

    fun getViewerData() {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO
        ) {
            try {
                val viewer = repository.getViewerData()
                viewerData.postValue(viewer)
            } catch (e: ApolloException) {
                println("Error fetching viewer data: $e")
            }
        }
    }

    fun observeViewer(): LiveData<ViewerQuery.Viewer?> {
        return viewerData
    }
    
    fun observeSearchMediaResults(): LiveData<List<MediaListQuery.Medium?>?> {
        return searchMediaResults
    }

    fun clearMedia() {
        media.postValue(null)
    }

    fun doAnimeDetails(context: Context, anime: MediaListQuery.Medium?) {
        val startDate =
            "${anime?.startDate?.month} / ${anime?.startDate?.day} / ${anime?.startDate?.year}"
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
            intent.putExtra(AnimeDetails.animePicKey, anime.coverImage?.extraLarge)
            intent.putExtra(AnimeDetails.animeAverageScore, anime.averageScore.toString())
            intent.putExtra(AnimeDetails.animeFavorites, anime.favourites.toString())
            intent.putExtra(AnimeDetails.animePopularity, anime.popularity.toString())
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

    fun doAnimeDetails(context: Context, anime: ViewerQuery.Node?) {
        val startDate =
            "${anime?.startDate?.month} / ${anime?.startDate?.day} / ${anime?.startDate?.year}"
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
            intent.putExtra(AnimeDetails.animePicKey, anime.coverImage?.extraLarge)
            intent.putExtra(AnimeDetails.animeAverageScore, anime.averageScore.toString())
            intent.putExtra(AnimeDetails.animeFavorites, anime.favourites.toString())
            intent.putExtra(AnimeDetails.animePopularity, anime.popularity.toString())
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