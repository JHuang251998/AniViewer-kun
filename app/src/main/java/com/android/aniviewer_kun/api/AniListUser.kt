package com.android.aniviewer_kun.api

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import com.android.aniviewer_kun.MainViewModel
import okhttp3.internal.applyConnectionSpec
import java.io.File

object AniListUser {
    var token: String? = null
    var userName: String? = null
    var userID: Int? = null
    var avatar: String? = null

    fun loginIntent(context: Context) {
        val clientID = 9991

        try {
            CustomTabsIntent.Builder().build().launchUrl(
                context,
                Uri.parse("https://anilist.co/api/v2/oauth/authorize?client_id=${clientID}&response_type=token")
            )
        } catch (e: ActivityNotFoundException) {
            Log.e(javaClass.simpleName, e.toString())
        }
    }

    fun logout(context: Context) {
        token = null
        userName = null
        userID = null
        avatar = null

        if ("anilistToken" in context.fileList()) {
            File(context.filesDir, "anilistToken").delete()
        }
    }
}
