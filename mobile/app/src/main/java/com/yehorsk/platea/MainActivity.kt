package com.yehorsk.platea

import android.Manifest
import android.app.LocaleManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.yehorsk.platea.auth.presentation.login.LoginViewModel
import com.yehorsk.platea.core.navigation.Graph
import com.yehorsk.platea.core.navigation.RootNavGraph
import com.yehorsk.platea.ui.theme.MobileTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import androidx.core.content.ContextCompat.getContextForLanguage
import com.yehorsk.platea.core.data.remote.UserRoles
import com.yehorsk.platea.core.presentation.MainScreenViewModel
import com.yehorsk.platea.core.utils.snackbar.LocalSnackbarHostState
import com.yehorsk.platea.core.utils.snackbar.ObserveAsEvents
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.toString
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val mainScreenViewModel: MainScreenViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(getContextForLanguage(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission()
        Timber.plant(Timber.DebugTree())

        installSplashScreen().apply {
            setKeepOnScreenCondition{ loginViewModel.uiState.value.isAuthenticating }
        }
        enableEdgeToEdge()
        setContent {

            val themeState by mainScreenViewModel.uiState.collectAsStateWithLifecycle()

            setLocale(themeState.language, this)

            val snackbarHostState = remember {
                SnackbarHostState()
            }

            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
                scope.launch{
                    snackbarHostState.currentSnackbarData?.dismiss()

                    val result = snackbarHostState.showSnackbar(
                        message = if(event.error != null) event.error.toString(context) else event.message!!,
                        actionLabel = event.action?.name,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                    if(result == SnackbarResult.ActionPerformed){
                        event.action?.action?.invoke()
                    }
                }
            }

            MobileTheme(
                isDarkMode = themeState.isDarkMode
            ) {
                val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
                val role by loginViewModel.userRole.collectAsStateWithLifecycle()

                LaunchedEffect(uiState.isLoggedIn) {
                    if(uiState.isLoggedIn){
                        mainScreenViewModel.refreshData()
                    }
                }

                val (startDestination, userRoles) = when {
                    uiState.isLoggedIn -> when (role.toString()) {
                        "user" -> Pair(Graph.Client, UserRoles.USER)
                        "waiter" -> Pair(Graph.Waiter, UserRoles.WAITER)
                        "admin" -> Pair(Graph.Admin, UserRoles.ADMIN)
                        "chef" -> Pair(Graph.Chef, UserRoles.CHEF)
                        else -> Pair(Graph.Authentication, UserRoles.AUTH)
                    }
                    else -> Pair(Graph.Authentication, UserRoles.AUTH)
                }
                CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        if (!uiState.isAuthenticating) {
                            val navController = rememberNavController()
                            RootNavGraph(
                                navController = navController,
                                loginViewModel = loginViewModel,
                                startDestination = startDestination,
                                userRoles = userRoles,
                                mainScreenViewModel = mainScreenViewModel
                            )
                        }
                    }
                }
            }
        }
    }


    private fun setLocale(language: String, context: Context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(language)
        }else{
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
        }
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
