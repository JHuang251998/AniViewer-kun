package com.android.aniviewer_kun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.aniviewer_kun.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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
                R.id.nav_airing
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.getMedia()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(activityMainBinding.root)
//
//        // setup nav drawer
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//        //val appBarConfiguration = AppBarConfiguration(topLevelDestinationIds = setOf(), fallbackOnNavigateUpListener  = ::onSupportNavigateUp)
//        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
//        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
////        val toolbar = findViewById<Toolbar>(R.id.toolbar)
////            .setupWithNavController(navController, appBarConfiguration) as Toolbar
////        val layout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
////        layout.setupWithNavController(toolbar, navController, appBarConfiguration)
//
//        findViewById<NavigationView>(R.id.nav_view)
//            .setupWithNavController(navController)
//
//        addHomeFragment()
//        viewModel.getMedia()
//    }
//
//    private fun addHomeFragment() {
//        supportFragmentManager.commit {
//            add(R.id.nav_host_fragment, AiringFragment())
//            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(topLevelDestinationIds = setOf(), fallbackOnNavigateUpListener  = ::onSupportNavigateUp)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}