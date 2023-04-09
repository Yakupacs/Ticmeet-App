package com.mtaner.android_ticmeet.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mtaner.android_ticmeet.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
    }
}