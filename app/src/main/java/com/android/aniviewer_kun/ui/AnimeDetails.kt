package com.android.aniviewer_kun.ui

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.android.aniviewer_kun.R
import com.android.aniviewer_kun.databinding.ActivityAnimeDetailsBinding
import com.android.aniviewer_kun.glide.Glide

class AnimeDetails : AppCompatActivity() {
    companion object {
        const val animeTitleRomajiKey = "titleRomaji"
        const val animePicKey = "image"
        const val animeAverageScore = "averageScore"
        const val animeFavorites = "favorites"
        const val animePopularity = "popularity"
        const val animeDescription = "description"
        const val animeFormat = "format"
        const val animeStartDate = "startDate"
        const val animeEndDate = "endDate"
        const val animeOtherNames = "otherNames"
        const val animeCountryOfOrigin = "countryOfOrigin"
        const val animeGenres = "genres"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animeDetailsBinding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(animeDetailsBinding.root)
//        setSupportActionBar(animeDetailsBinding.animeDetailsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val activityThatCalled = intent
        val callingBundle = activityThatCalled.extras

        val titleRomajiText = callingBundle?.getString(animeTitleRomajiKey)
        val imageURL = callingBundle?.getString(animePicKey)
        val averageScore = callingBundle?.getInt(animeAverageScore)
        val favorites = callingBundle?.getInt(animeFavorites)
        val popularity = callingBundle?.getInt(animePopularity)

        var description = callingBundle?.getString(animeDescription)
        if (description != null) {
            description = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.escapeHtml(description)
            }
        }

        val animeType = callingBundle?.getString(animeFormat)
        val startDate = callingBundle?.getString(animeStartDate)
        val endDate = callingBundle?.getString(animeEndDate)
        val otherNames = callingBundle?.getString(animeOtherNames)
        val origin = callingBundle?.getString(animeCountryOfOrigin)
        val genres = callingBundle?.getString(animeGenres)

        supportActionBar?.title = titleRomajiText

        animeDetailsBinding.contentAnimeDetails.romajiTitle.text = titleRomajiText
        animeDetailsBinding.contentAnimeDetails.averageScore.text = "Score: ${averageScore}%"
        animeDetailsBinding.contentAnimeDetails.favorites.text = favorites.toString()
        animeDetailsBinding.contentAnimeDetails.popularity.text = popularity.toString()

        animeDetailsBinding.contentAnimeDetails.detailedDescription.text = description
        animeDetailsBinding.contentAnimeDetails.detailedDescription.movementMethod = ScrollingMovementMethod()

        animeDetailsBinding.contentAnimeDetails.mediaFormat.text = animeType
        animeDetailsBinding.contentAnimeDetails.startDate.text = startDate
        animeDetailsBinding.contentAnimeDetails.endDate.text = endDate
        animeDetailsBinding.contentAnimeDetails.otherNames.text = otherNames
        animeDetailsBinding.contentAnimeDetails.origin.text = origin
        animeDetailsBinding.contentAnimeDetails.genres.text = genres

        if (imageURL != null) {
            Glide.glideFetch(
                imageURL,
                animeDetailsBinding.contentAnimeDetails.subRowPic
            )
        } else {
            animeDetailsBinding.contentAnimeDetails.subRowPic.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}