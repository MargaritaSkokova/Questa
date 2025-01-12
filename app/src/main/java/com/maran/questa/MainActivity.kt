package com.maran.questa

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.maran.questa.navigation.NavigationStack
import com.maran.questa.network.apis.SecurityApi
import com.maran.questa.network.apis.ThemeApi
import com.maran.questa.network.models.Model
import com.maran.questa.network.retrofitClient
import com.maran.questa.network.retrofitClientAuthentication
import com.maran.questa.ui.theme.QuestaTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofitAuth = retrofitClientAuthentication()
        val securityService = retrofitAuth.create(SecurityApi::class.java)
        val preferenceFile = getPreferences(MODE_PRIVATE)

        val job = lifecycleScope.launch {
            val token = securityService.login(Model.Authentication("xxx", "xxx"))
            with(preferenceFile.edit()) {
                putString("token", token)
                apply()
            }
        }

        lifecycleScope.launch {
            job.join()
            val retrofit = retrofitClient(preferenceFile.getString("token", "")!!)
            val themeService = retrofit.create(ThemeApi::class.java)
            val themes = themeService.getAll()
            Log.v("retrofit debug", themes.toString())
        }

        enableEdgeToEdge()
        setContent {
            QuestaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationStack(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

