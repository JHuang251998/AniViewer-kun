package com.android.aniviewer_kun

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.android.aniviewer_kun.databinding.ActivityAnimeDetailsBinding
import com.android.aniviewer_kun.databinding.ContentAnimeDetailsBinding
import com.android.aniviewer_kun.databinding.FragmentAnimeDetailsBinding
import com.android.aniviewer_kun.databinding.FragmentSingleImageBinding
import com.android.aniviewer_kun.glide.Glide
import com.android.aniviewer_kun.ui.AnimeDetailsFragment
import com.android.aniviewer_kun.ui.SingleImageFragment

class AnimeDetails : AppCompatActivity() {
    companion object {
        const val animeTitleRomajiKey = "titleRomaji"
        const val animePicKey = "imageURL"
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

    private lateinit var appBarConfiguration: AppBarConfiguration

    // https://developer.android.com/develop/ui/views/layout/immersive
    fun hideBars() {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        // XXX Write me (one liner)
        supportActionBar?.hide()
    }

    fun showBars() {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Hide both the status bar and the navigation bar
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        supportActionBar?.show()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun setActionBarBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animeDetailsBinding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(animeDetailsBinding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navController = findNavController(R.id.nav_host_fragment_content_anime_details)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val activityThatCalled = intent
        val callingBundle = activityThatCalled.extras

        val titleRomajiText = callingBundle?.getString(animeTitleRomajiKey)
        val imageURL = callingBundle?.getString(animePicKey)
        val averageScore = callingBundle?.getString(animeAverageScore)
        val favorites = callingBundle?.getString(animeFavorites)
        val popularity = callingBundle?.getString(animePopularity)

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

        val bundle = Bundle()
        bundle.putString(animeTitleRomajiKey, titleRomajiText)
        bundle.putString(animePicKey, imageURL)
        bundle.putString(animeAverageScore, averageScore)
        bundle.putString(animeFavorites, favorites)
        bundle.putString(animePopularity, popularity)
        bundle.putString(animeDescription, description)
        bundle.putString(animeFormat, animeType)
        bundle.putString(animeStartDate, startDate)
        bundle.putString(animeEndDate, endDate)
        bundle.putString(animeOtherNames, otherNames)
        bundle.putString(animeCountryOfOrigin, origin)
        bundle.putString(animeGenres, genres)

        navController.setGraph(R.navigation.anime_details_nav_graph, bundle)
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}