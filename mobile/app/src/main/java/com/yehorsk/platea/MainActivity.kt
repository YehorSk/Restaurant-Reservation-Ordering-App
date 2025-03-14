package com.yehorsk.platea

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.yehorsk.platea.auth.presentation.login.LoginViewModel
import com.yehorsk.platea.core.navigation.Graph
import com.yehorsk.platea.core.navigation.RootNavGraph
import com.yehorsk.platea.ui.theme.MobileTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startTime = System.currentTimeMillis()

        requestNotificationPermission()
        Timber.plant(Timber.DebugTree())

        installSplashScreen().apply {
            setKeepOnScreenCondition{ loginViewModel.uiState.value.isAuthenticating }
        }
        enableEdgeToEdge()
        setContent {
            MobileTheme {
                val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
                val role by loginViewModel.userRole.collectAsStateWithLifecycle()
                val startDestination = when {
                    uiState.isLoggedIn -> when (role.toString()) {
                        "user" -> Graph.HOME
                        "waiter" -> Graph.WAITER
                        "admin" -> Graph.ADMIN
                        "chef" -> Graph.CHEF
                        else -> Graph.AUTHENTICATION
                    }
                    else -> Graph.AUTHENTICATION
                }
                Surface(modifier = Modifier.fillMaxSize()) {
                    if(!uiState.isAuthenticating){
                        RootNavGraph(
                            navController = rememberNavController(),
                            loginViewModel = loginViewModel,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
        val endTime = System.currentTimeMillis()
        Timber.d("MainActivity startup time: ${endTime - startTime} ms")
    }

    private fun requestNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if(!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

}
