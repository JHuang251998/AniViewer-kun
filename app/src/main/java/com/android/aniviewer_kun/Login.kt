package com.android.aniviewer_kun

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.aniviewer_kun.api.AniListUser

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: Uri? = intent?.data
        Log.d("XXX", "HI")
        Log.d("XXX", data.toString())
        try {
            AniListUser.token = Regex("""(?<=access_token=).+(?=&token_type)""").find(data.toString())!!.value
            Log.d("XXX", AniListUser.token.toString())
            val filename = "anilistToken"
            this.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(AniListUser.token!!.toByteArray())
            }
        } catch (e: Exception) {
            println("Error logging in: $e")
        }

        this.finishAffinity()
        this.startActivity(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}