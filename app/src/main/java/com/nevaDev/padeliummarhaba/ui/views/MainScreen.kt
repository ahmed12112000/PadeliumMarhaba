package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.nevadev.padeliummarhaba.R
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onReservationClicked: (LocalDate) -> Unit,
    viewModel: KeyViewModel = hiltViewModel(),

    ) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var isButtonClicked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isConnected = remember { mutableStateOf(checkInternetConnection(context)) }
    val images = listOf(R.drawable.a, R.drawable.b, R.drawable.c)

    LaunchedEffect(Unit) {
        while (true) {
            isConnected.value = checkInternetConnection(context)
            delay(5000)
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -4.dp)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(Color(0xFF0054D8))
                .height(150.dp)
        ) {}

        Button(
            onClick = {
                if (isConnected.value) {
                    if (!isButtonClicked ) {
                        isButtonClicked = true
                        selectedDate = LocalDate.now()
                        selectedDate?.let { date ->
                            isLoading = true
                            onReservationClicked(date)

                        }
                    }
                } else {
                    showToastForFiveSeconds(context, "Veuillez vérifier votre connexion Internet")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor =  Color(0xFFD7F057)
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .offset(x = 100.dp, y = -35.dp)
                .border(0.5.dp, Color(0xFFD7F057), RoundedCornerShape(12.dp))
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = "Réserver",
                color = Color(0xFF0054D8) ,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.pictures_gallerie),
                contentDescription = "Logo",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "|",
                color = Color(0xFF0054D8),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Photos",
                color = Color(0xFF0054D8),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        ImageCarousel(
            images = images,
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .height(335.dp)
        )
    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(images: List<Int>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(5.dp)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp).offset(y = -71.dp),
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            images.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (index == pagerState.currentPage) Color(0xFF0054D8) else Color.White)
                )
            }
        }
    }
}