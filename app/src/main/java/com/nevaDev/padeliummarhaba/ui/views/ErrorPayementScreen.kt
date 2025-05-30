package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.nevadev.padeliummarhaba.R

@Composable
fun ErrorPayementScreen(navController: NavController? = null)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0C1B)), // Dark background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            // Logo Header (PADELIUM MARHABA)
            Text(
                text = "PADELIUM\nMARHABA",
                color = Color(0xFFE5FF00),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            // 404 Image with custom look (you may need to use painterResource for local image)
            Image(
                painter = painterResource(id = R.drawable.image404), // Replace with your image
                contentDescription = "404",
                modifier = Modifier.size(200.dp)
            )

            // Ooops text
            Text(
                text = "Ooops",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Description
            Text(
                text = "Choisissez le court qui vous passionne et pratiquez le padel à votre rythme !",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            // Button to return
            Button (
                onClick = { navController?.navigate("main_screen") },

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0054D8), // background color
                    contentColor = Color.White         // text/icon color
                ),

                        shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Retour à l'accueil")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorPayementScreenPreview() {
    ErrorPayementScreen()
}



