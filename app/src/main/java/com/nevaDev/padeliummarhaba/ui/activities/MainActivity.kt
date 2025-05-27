package com.nevaDev.padeliummarhaba.ui.activities


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nevaDev.padeliummarhaba.ui.views.SplashScreen
import com.nevaDev.padeliummarhaba.ui.theme.PadeliumMarhabaTheme
import dagger.hilt.android.AndroidEntryPoint
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material.icons.filled.SignalWifiOff
import com.nevaDev.padeliummarhaba.di.NetworkUtil
import androidx.compose.ui.text.TextStyle
import com.nevaDev.padeliummarhaba.ui.views.AnimatedBottomBar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var networkUtil: NetworkUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkUtil = NetworkUtil(this)

        setContent {
            PadeliumMarhabaTheme {
                var showSplashScreen by remember { mutableStateOf(true) }
                var isNetworkAvailable by remember { mutableStateOf(true) }
                val navController = rememberNavController()
                val viewModel: GetProfileViewModel = hiltViewModel()
                val context = LocalContext.current
                val sessionManager = remember { SessionManager(context) }
                val sharedPreferences = remember { context.getSharedPreferences("csrf_prefs", MODE_PRIVATE) }

                LaunchedEffect(Unit) {
                    networkUtil.registerNetworkCallback { isConnected ->
                        isNetworkAvailable = isConnected
                        if (!isNetworkAvailable) {
                            Toast.makeText(context, "Internet disconnected", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                LaunchedEffect(Unit) {
                    delay(3000)
                    showSplashScreen = false
                }

                if (showSplashScreen) {
                    SplashScreen()
                } else {
                    val onLogout: () -> Unit = {
                        sharedPreferences.edit().clear().apply()
                        sessionManager.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }

                    MainApp(
                        context = context,
                        sharedPreferences = sharedPreferences,
                        viewModel = viewModel,
                        navController = navController,
                        isNetworkAvailable = isNetworkAvailable
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkUtil.unregister()
    }
}

@Composable
fun MainApp(
    context: Context,
    sharedPreferences: SharedPreferences,
    viewModel: GetProfileViewModel,
    navController: NavHostController,
    isNetworkAvailable: Boolean
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val getReservationViewModel: GetReservationViewModel = hiltViewModel()
    val sessionManager = remember { SessionManager(context) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedItem = currentBackStackEntry?.destination?.route
    val shouldShowBottomBar = selectedItem !in listOf("splash_screen", "login_screen")

    val interactionSource = remember { MutableInteractionSource() }


    val showDialog = remember { mutableStateOf(!isNetworkAvailable) }

    LaunchedEffect(isNetworkAvailable) {
        if (!isNetworkAvailable) {
            showDialog.value = true
        } else {
            showDialog.value = false
        }
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                AnimatedBottomBar(
                    navController = navController,
                    getReservationViewModel = getReservationViewModel,

                )
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)
            ) {
                if (isNetworkAvailable) {
                    AppNavHost(
                        navController = navController,
                        isUserLoggedIn = sessionManager.isLoggedIn(),
                        onLoginSuccess = { },
                        onLogout = { sessionManager.logout() },
                        context = context,
                        sharedPreferences = sharedPreferences,
                        drawerState = drawerState,
                        scope = scope,
                        onSignupSuccess = { }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable(interactionSource = interactionSource, indication = null) {}
                    )
                }
            }
        }
    )

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = true },
            title = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SignalWifiOff,
                        contentDescription = "No Internet",
                        tint = Color.Red,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Aucune connexion internet",
                        style = TextStyle(fontSize = 18.sp, color = Color.Black)
                    )
                }
            },
            text = {
                Text("Veuillez vérifier votre connexion Internet et réessayer.")
            },
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if (isNetworkAvailable) {
                                showDialog.value = false
                            } else {
                                showDialog.value = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  Color(0xFF0054D8)
                        ),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Réessayez", color = Color.White)
                    }
                }
            }
        )
    }
}
