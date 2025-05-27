package com.nevaDev.padeliummarhaba.ui.views


import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.viewmodels.CreditPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetPacksViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentGetAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UserAvoirViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetPacksResponse
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.UserAvoirRequest
import com.padelium.domain.dto.UserAvoirResponse
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun CreditCharge(
    viewModel: GetPacksViewModel = hiltViewModel(),
    userAvoirViewModel: UserAvoirViewModel = hiltViewModel(),
    creditPayViewModel: CreditPayViewModel = hiltViewModel(),
    navController: NavController
) {

    val packsData by viewModel.packsData.observeAsState(DataResult.Loading)
    val paymentResponse by userAvoirViewModel.dataResult.observeAsState(DataResult.Loading)
    var isLoading by remember { mutableStateOf(false) }
    var selectedPackId by remember { mutableStateOf<Int?>(null) }
    var selectedAmount by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(Unit) {
        viewModel.GetPacks()
        creditPayViewModel.GetCreditPay()
    }
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFE5E5E5)).verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF0066CC)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nos Plans",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFCCFF00)
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = "Découvrez nos offres tarifaires adaptées à vos besoins !",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (val result = packsData) {
                    is DataResult.Success -> {
                        val packs = result.data as? List<GetPacksResponse>
                        if (packs != null) {
                            packs.forEach { pack ->
                                PricingCard(
                                    title = pack.title,
                                    price = pack.description,
                                    credits = pack.amount.toString(),
                                    currencySymbol = pack.currency.currencySymbol,
                                    amount = pack.amount,
                                    currency = "DT",
                                    orderId = "520",
                                    onPaymentClick = { userAvoirRequest ->
                                        selectedPackId = pack.id.toInt()
                                        selectedAmount = pack.amount.toDouble()
                                        userAvoirViewModel.PaymentAvoir(userAvoirRequest)
                                    }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        } else {
                            Text(text = "No packs available")
                        }
                    }
                    is DataResult.Loading -> {
                        Text(text = "Loading...")
                    }
                    is DataResult.Failure -> {
                        result.errorCode?.let { errorCode ->
                            if (errorCode != 200) {
                                navController.navigate("server_error_screen")
                            } else {
                                Text(text = "Error: ${result.errorMessage}")
                            }
                        } ?: run {
                            Text(text = "Error: ${result.errorMessage}")
                        }
                    }
                }
            }
        }

        LaunchedEffect(paymentResponse) {
            if (paymentResponse is DataResult.Success) {
                val paymentData = (paymentResponse as DataResult.Success).data as? UserAvoirResponse
                val formUrl = paymentData?.formUrl

                if (!formUrl.isNullOrEmpty() && selectedAmount != null && selectedPackId != null) {
                    val encodedUrl = Uri.encode(formUrl)
                    val encodedAmount = Uri.encode(selectedAmount.toString())
                    val encodedId = Uri.encode(selectedPackId.toString())

                    Log.d("Navigation", "Navigating to WebViewScreen1 with URL: $formUrl")
                    navController.navigate("WebViewScreen1?formUrl=$encodedUrl&encodedAmount=$encodedAmount&encodedId=$encodedId")
                    Log.d("Navigation", "Navigating to WebViewScreen1 with URL: $encodedId")

                } else {
                    isLoading = false
                    Log.e("Navigation", "Error: Missing form URL or amount.")
                }
            }
        }
    }
}

@Composable
fun PricingCard(
    title: String,
    price: String,
    credits: String,
    currencySymbol: String,
    amount: BigDecimal,
    currency: String,
    orderId: String,
    onPaymentClick: (UserAvoirRequest) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        elevation = 4.dp,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 23.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val userAvoirRequest = UserAvoirRequest(
                        amount = amount,
                        currency = "TND",
                        orderId = null.toString()
                    )
                    onPaymentClick(userAvoirRequest)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0066CC),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "$credits $currencySymbol",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}








