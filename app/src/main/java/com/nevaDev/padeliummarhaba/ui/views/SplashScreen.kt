package com.nevaDev.padeliummarhaba.ui.views

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun SplashScreen() {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0054D8)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = -200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logopadelium),
                contentDescription = "Padelium Marhaba Logo",
                modifier = Modifier
                    .size(410.dp)
                    .offset(y = 180.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Le Padel dans son excellence",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp)
                    .offset(y = 190.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.Cyan,
                    strokeWidth = 4.dp,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(bottom = 32.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y=185.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "v1.0.0.47",
                        fontSize = 10.sp,
                        color = Color.White
                    )

                    CopyrightText()
                }
            }
        }
    }

}


@Composable
fun CopyrightText() {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        append("Copyright © 2025 ")
        pushStringAnnotation(tag = "SPOFUN", annotation = "https://spofun.tn/")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
            append("SPOFUN")
        }
        pop()
        append(" | Tous droits réservés.")
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            val clickedTag = annotatedString.getStringAnnotations(offset, offset).firstOrNull()
            clickedTag?.let {
                if (it.tag == "SPOFUN") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(intent)
                }
            }
        },
        style = TextStyle(color = Color.White, fontSize = 10.sp)
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}