package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.repositories.IGetReservationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlin.io.path.Path
import kotlin.io.path.moveTo

@Composable
fun AnimatedBottomBar(
    navController: NavController,
    getReservationViewModel: GetReservationViewModel,
    modifier: Modifier = Modifier,

) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination



    val navItems = listOf(
        NavItem("main_screen", Icons.Filled.Home, "Accueil"),
        NavItem("summary_screen", Icons.Filled.CalendarMonth, "Mes Réservations"),
        NavItem("CreditPayment", Icons.Filled.CreditCard, "Mes crédits"),
        NavItem("Profile_screen", Icons.Filled.Person, "Profil")
    )
    val selectedItem = currentDestination?.route?.let { route ->
        navItems.find { it.route == route || route.startsWith("${it.route}/") }?.route
    }

    val targetIndex = navItems.indexOfFirst { it.route == selectedItem }.coerceAtLeast(0)
    val animatedIndex by animateFloatAsState(
        targetValue = targetIndex.toFloat(),
        animationSpec = tween(durationMillis = 500, easing = EaseInOut)
    )

    Box(
        modifier = modifier

            .then(
                if (selectedItem != null) {
                    Modifier.fillMaxWidth().height(105.dp)

                } else {
                    Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .background(
                            color = Color(0xFF0054D8),
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0054D8))
        )
        if (selectedItem != null) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -2.dp)
                .height(80.dp)
                .align(Alignment.TopCenter)
        ) {
            val itemWidth = size.width / navItems.size
            val centerX = animatedIndex * itemWidth + itemWidth / 2

            val waveSideLength = itemWidth / 1.6f
            val waveHeight = size.height * 0.58f
            val peakHeight = size.height * 0.15f

            val wavePath = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, waveHeight)

                lineTo(centerX - waveSideLength, waveHeight)

                cubicTo(
                    centerX - waveSideLength * 0.6f, waveHeight,
                    centerX - waveSideLength * 0.5f, peakHeight,
                    centerX, peakHeight
                )

                cubicTo(
                    centerX + waveSideLength * 0.5f, peakHeight,
                    centerX + waveSideLength * 0.6f, waveHeight,
                    centerX + waveSideLength, waveHeight
                )

                lineTo(size.width, waveHeight)
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(
                path = wavePath,
                color = Color.White
            )
        }
}
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y = -8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            navItems.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    CustomBottomNavItem(
                        navController = navController,
                        route = item.route,
                        icon = item.icon,
                        label = item.label,
                    )
                }
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun AnimatedBottomBarPreview() {
    val mockNavController = rememberNavController()

    val fakeRepository = object : IGetReservationRepository {
        override suspend fun GetReservation(): List<GetReservationResponse> {
            return emptyList()
        }
    }

    val fakeViewModel = GetReservationViewModel(fakeRepository)

    AnimatedBottomBar(
        navController = mockNavController,
        getReservationViewModel = fakeViewModel
    )
}


@Composable
fun CustomBottomNavItem(
    navController: NavController,
    route: String,
    icon: ImageVector,
    label: String,
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val isConnected = remember { mutableStateOf(checkInternetConnection(context)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    LaunchedEffect(Unit) {
        while (true) {
            isConnected.value = checkInternetConnection(context)
            delay(5000)
        }
    }
    val actualIsSelected = currentDestination?.route?.let {
        it == route || it.startsWith("$route/")
    } ?: false

    val restrictedRoutes = listOf("Profile_screen", "CreditPayment", "summary_screen")

    val animatedScale by animateFloatAsState(
        targetValue = if (actualIsSelected) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )

    val animatedOffsetY by animateDpAsState(
        targetValue = if (actualIsSelected) (-13).dp else 1.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (actualIsSelected) Color(0xFFD7F057) else Color.Transparent,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )

    val iconColor by animateColorAsState(
        targetValue = if (actualIsSelected) Color(0xFF0054D8) else Color.White,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )

    val textColor by animateColorAsState(
        targetValue = if (actualIsSelected) Color(0xFFD7F057) else Color.White,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )
    val spacerHeight by animateDpAsState(
        targetValue = if (actualIsSelected) 8.dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable {
                if (!isConnected.value) {
                    showToastForFiveSeconds(context, "Pas de connexion internet")
                } else if (route in restrictedRoutes && !sessionManager.isLoggedIn()) {
                    if (!sessionManager.isSessionValid()) {
                        sessionManager.clearAuthToken()
                    }
                    navController.navigate("login_screen?redirectUrl=$route") {
                        popUpTo("main_screen") { inclusive = false }
                    }
                } else {
                    navController.navigate(route)
                }
            }
            .offset(y = animatedOffsetY)

            .scale(animatedScale)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(spacerHeight))

        Text(
            text = label,
            color = textColor,
            fontSize = 9.sp,
            fontWeight = if (actualIsSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}






@Composable
fun TopBar(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFF0054D8))

    ) {


        Icon(
            painter = painterResource(id = R.drawable.logopadelium),
            contentDescription = "Right Icon",
            tint = Color.Unspecified,
            modifier = Modifier
                .fillMaxSize(0.6f)
                .clip(CircleShape)
                .clickable {
                    navController.navigate("main_screen")
                }
                .align(Alignment.CenterEnd)

        )



    }
}



fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun showToastForFiveSeconds(context: Context, message: String) {
    val handler = Handler(Looper.getMainLooper())
    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)

    val repeatCount = 3
    var count = 0

    val runnable = object : Runnable {
        override fun run() {
            if (count < repeatCount) {
                toast.show()
                count++
                handler.postDelayed(this, 1000)
            }
        }
    }
    handler.post(runnable)
}

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)