package com.android.aniviewer_kun

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.aniviewer_kun.api.AniListUser
import com.android.aniviewer_kun.databinding.ActivityMainBinding
import com.android.aniviewer_kun.glide.Glide
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // An Android nightmare
    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    // https://stackoverflow.com/questions/7789514/how-to-get-activitys-windowtoken-without-view
    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_airing,
                R.id.nav_top100,
                R.id.nav_search,
                R.id.nav_favourites
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.appBarMain.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    val i = Intent(this, SettingsManager::class.java)
                    TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(i)
                        .startActivities();
                }
            }
            true
        }

        val headerLayout = navView.getHeaderView(0)
        val loginButton = headerLayout.findViewById<Button>(R.id.loginButton)
        val userNameTV = headerLayout.findViewById<TextView>(R.id.username)
        val userIDTV = headerLayout.findViewById<TextView>(R.id.userID)
        val userAvatar = headerLayout.findViewById<ImageView>(R.id.avatar)

        if (AniListUser.token != null) {
            viewModel.getViewerData()
            loginButton.text = "LOGOUT"
            loginButton.setOnClickListener {
                AniListUser.logout(this)

                this.finishAffinity()
                this.startActivity(
                    Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            viewModel.observeViewer().observe(this) {
                userNameTV.text = "Username: ${AniListUser.userName}"
                userIDTV.text = "User ID: ${AniListUser.userID}"
                AniListUser.avatar?.let { it1 ->
                    Glide.glideFetch(
                        it1,
                        userAvatar
                    )
                }
            }
        } else {
            loginButton.text = "LOGIN"
            loginButton.setOnClickListener {
                AniListUser.loginIntent(this)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        println("onSupportNavigateUp")
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}