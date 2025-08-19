package com.nevaDev.padeliummarhaba.ui.views

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.viewmodels.ErrorCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetEmailViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetManagerViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetPaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentGetAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPartBookingViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate


@Composable
fun WebViewScreen1(
    navController: NavController,
    formUrl: String,
    paymentGetAvoirViewModel: PaymentGetAvoirViewModel,
    amount: BigDecimal,
    Id: Long
) {
    val context = LocalContext.current
    val amountList = amount.toPlainString().split(",").mapNotNull { it.toLongOrNull() }
    val IdValue = Id
    val scrollState = rememberScrollState()
    var isLoading by remember { mutableStateOf(true) }
    val isWebViewExpanded = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
        ) {
            AndroidView(factory = {
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            Log.d("WebViewScreen", "Loading URL: $url")
                            return super.shouldOverrideUrlLoading(view, url)
                        }
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                            val url = request.url.toString()
                            val orderId = extractOrderId1(url)
                            val amountBigDecimal = amountList.fold(BigDecimal.ZERO) { acc, amount ->
                                acc + BigDecimal(amount)
                            }
                            if (orderId.isNotEmpty()) {
                                val request1 = PaymentGetAvoirRequest(
                                    amount = amountBigDecimal,
                                    packId = IdValue,
                                    paymentRef = extractpaymentRef(url),
                                    orderId = orderId,
                                )
                                coroutineScope.launch {
                                    try {
                                        val response: Boolean = paymentGetAvoirViewModel.PaymentGetAvoir(request1, navController)
                                        if (response) {
                                            navController.navigate("PaymentSuccessScreen")
                                        } else {
                                            navController.navigate("payment_error_screen")
                                        }
                                    } catch (e: Exception) {
                                        navController.navigate("payment_error_screen")
                                    }
                                }
                            } else {
                                navController.navigate("payment_error_screen")
                            }

                            return true
                        }
                        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                            handler?.cancel()
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                    }
                    loadUrl(formUrl)
                }
            }, modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = {
                    isWebViewExpanded.value = false
                    navController.navigate("CreditCharge")
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close WebView",
                    tint = Color.Black
                )
            }
        }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier)
    }
}

fun extractpaymentRef(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("paymentRef") ?: ""
}

fun extractOrderId1(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("orderId") ?: ""
}

@Composable
fun WebViewScreen2(
    navController: NavController,
    viewmodel: PaymentPartBookingViewModel,
    formUrl: String,
    onReservationClicked: (LocalDate) -> Unit,

    ) {
    val backStackEntry = navController.currentBackStackEntry
    val context = LocalContext.current
    val BookingId = backStackEntry?.arguments?.getString("BookingId")?.toLongOrNull() ?: 0L
    val encodedPartnerPayId = backStackEntry?.arguments?.getString("encodedPartnerPayId")?.toLongOrNull() ?: 0L
    val navBackStackEntry = remember { navController.currentBackStackEntry }
    val privateListString = navBackStackEntry?.arguments?.getString("privateList") ?: ""
    val privateList = privateListString.split(",").mapNotNull { it.toLongOrNull() }.toMutableList()
    val isWebViewExpanded = remember { mutableStateOf(true) }
    val dataResult by viewmodel.dataResult.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    var isButtonClicked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
        ) {
            AndroidView(factory = {
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true


                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            Log.d("WebViewScreen", "Loading URL: $url")
                            return super.shouldOverrideUrlLoading(view, url)
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest
                        ): Boolean {
                            val url = request.url.toString()
                            val orderId = extractOrderId2(url)

                            if (orderId.isNotEmpty()) {
                                val request1 = PaymentPartBookingRequest(
                                    privateExtrasIds = privateList,
                                    id = BookingId,
                                    bookingId = encodedPartnerPayId,
                                    orderId = orderId,
                                )
                                coroutineScope.launch {
                                    try {
                                        val response: Boolean = viewmodel.PaymentPartBooking(request1, navController)
                                        if (response) {
                                            navController.navigate("PaymentSuccessScreen")
                                        } else {
                                            navController.navigate("payment_error_screen")
                                        }
                                    } catch (e: Exception) {
                                        navController.navigate("payment_error_screen")
                                    }
                                }
                            } else {
                                navController.navigate("payment_error_screen")
                            }

                            return true
                        }

                        override fun onReceivedSslError(
                            view: WebView?,
                            handler: SslErrorHandler?,
                            error: SslError?
                        ) {
                            handler?.cancel()
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                    }

                    loadUrl(formUrl)
                }
            }, modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = {


                    if (!isButtonClicked) {
                        isButtonClicked = true

                        navController.navigate("main_screen")
                    }

                    isWebViewExpanded.value = false
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close WebView",
                    tint = Color.Black
                )
            }
        }
    }
    dataResult?.let { result ->
        when (result) {
            is DataResult.Loading -> {
            }
            is DataResult.Success -> {
                navController.navigate("PaymentSuccessScreen")
            }

            is DataResult.Failure -> {
            }
        }
    }
}

fun extractOrderId2(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("orderId") ?: ""
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WebViewScreen(
    navController: NavController,
    formUrl: String,
    getPaymentViewModel: GetPaymentViewModel,
    getManagerViewModel: GetManagerViewModel,
    getEmailViewModel: GetEmailViewModel,
    numberOfPart: Int,
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
    userIds: String,
    sharedList: String,
    privateList: String,
    bookingIds: String,
    onReservationClicked: (LocalDate) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userIdsList = userIds.split(",").mapNotNull { it.toLongOrNull() }
    val sharedListIds = sharedList.split(",").mapNotNull { it.toLongOrNull() }
    val privateListIds = privateList.split(",").mapNotNull { it.toLongOrNull() }
    val isWebViewExpanded = remember { mutableStateOf(true) }
    val errorCreditViewModel: ErrorCreditViewModel = hiltViewModel()
    val bookingIdsList = bookingIds.split(",").mapNotNull { it.toLongOrNull() }
    var isButtonClicked by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
        ) {
            AndroidView(
                factory = {
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                url: String?
                            ): Boolean {
                                url?.let {
                                    if (it.contains("paymentSuccess")) {
                                        navController.navigate("PaymentSuccessScreen")
                                        return true
                                    }
                                }
                                return super.shouldOverrideUrlLoading(view, url)
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest
                            ): Boolean {
                                val url = request.url.toString()

                                val originalUri = Uri.parse(formUrl)
                                val numberOfPartValue =
                                    originalUri.getQueryParameter("numberOfPart")
                                        ?: numberOfPart.toString()

                                val newUri = Uri.parse(url).buildUpon()
                                    .appendQueryParameter("numberOfPart", numberOfPartValue)
                                    .build()

                                val orderId = extractOrderId(url)
                                val bookingIds = extractBookingIds(url)
                                val numberOfPartValueNew = extractNumberOfPart(newUri.toString())

                                if (orderId.isNotEmpty()) {
                                    val request = GetPaymentRequest(
                                        bookingIds = bookingIds,
                                        couponIds = extractCouponIds(newUri.toString()),
                                        numberOfPart = numberOfPartValueNew,
                                        orderId = orderId,
                                        privateExtrasIds = privateListIds,
                                        sharedExtrasIds = sharedListIds,
                                        userIds = userIdsList
                                    )

                                    coroutineScope.launch {
                                        try {
                                            val response: Boolean = getPaymentViewModel.GetPayment2(request, navController) // Ensure this returns Boolean
                                            if (response) {
                                                getManagerViewModel.GetManager(bookingIds)
                                                getEmailViewModel.GetEmail(bookingIds)
                                                navController.navigate("PaymentSuccessScreen")
                                            } else {
                                                val creditErrorRequest = CreditErrorRequest(
                                                    amount = BigDecimal.ZERO,
                                                    bookingIds = bookingIdsList,
                                                    buyerId = 0L,
                                                    payFromAvoir = false,
                                                    status = true,
                                                    token = "",
                                                    transactionId = 0L
                                                )

                                                errorCreditViewModel.ErrorCredit(creditErrorRequest)
                                                navController.navigate("payment_error_screen")
                                            }
                                        } catch (e: Exception) {
                                        }
                                    }
                                } else {
                                }

                                view?.loadUrl(newUri.toString())
                                return true
                            }

                            override fun onReceivedSslError(
                                view: WebView?,
                                handler: SslErrorHandler?,
                                error: SslError?
                            ) {
                                handler?.cancel()
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                            }
                        }

                        val uri = Uri.parse(formUrl)
                        val updatedUrl =
                            if (uri.getQueryParameter("numberOfPart").isNullOrEmpty()) {
                                "$formUrl&numberOfPart=$numberOfPart"
                            } else {
                                val newUri = uri.buildUpon()
                                    .appendQueryParameter("numberOfPart", numberOfPart.toString())
                                    .build()
                                newUri.toString()
                            }
                        loadUrl(updatedUrl)
                    }
                }, modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = {
                    val creditErrorRequest = CreditErrorRequest(
                        amount = BigDecimal.ZERO,
                        bookingIds = bookingIdsList,
                        buyerId = 0L,
                        payFromAvoir = false,
                        status = true,
                        token = "",
                        transactionId = 0L
                    )
                    coroutineScope.launch {
                        errorCreditViewModel.ErrorCredit(creditErrorRequest)
                    }

                    if (!isButtonClicked) {
                        isButtonClicked = true

                        navController.navigate("main_screen")
                    }

                    isWebViewExpanded.value = false
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close WebView",
                    tint = Color.Black
                )
            }
        }

    }
    val errorDataResult by errorCreditViewModel.dataResult.observeAsState()
    errorDataResult?.let { result ->
        when (result) {
            is DataResult.Loading -> {
            }

            is DataResult.Success -> {
            }

            is DataResult.Failure -> {
            }
        }
    }

}

fun extractBookingIds(url: String): List<Long> {
    val uri = Uri.parse(url)
    val ids = uri.getQueryParameter("bookingIds")
    if (ids != null) {
        return ids.split(",").mapNotNull { it.trim().toLongOrNull() }
    }

    val pathSegments = uri.pathSegments
    val fallbackId = pathSegments.getOrNull(3)?.toLongOrNull()
    return if (fallbackId != null) listOf(fallbackId) else emptyList()
}

fun extractOrderId(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("orderId") ?: ""
}

fun extractNumberOfPart(url: String): Int {
    val uri = Uri.parse(url)
    val numberOfPartRaw = uri.getQueryParameter("numberOfPart")
    return numberOfPartRaw?.toIntOrNull() ?: 1
}

fun extractCouponIds(url: String): Map<Long, Long> {
    val uri = Uri.parse(url)
    val ids = uri.getQueryParameter("couponIds")
    return ids?.split(",")?.mapNotNull {
        val pair = it.split(":")
        if (pair.size == 2) {
            val key = pair[0].toLongOrNull()
            val value = pair[1].toLongOrNull()
            if (key != null && value != null) key to value else null
        } else null
    }?.toMap() ?: emptyMap()
}