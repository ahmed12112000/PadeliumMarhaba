package com.nevaDev.padeliummarhaba.ui.activities


import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.gson.reflect.TypeToken
import com.padelium.data.dto.ReservationOption
import com.nevaDev.padeliummarhaba.ui.views.*
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetEmailViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetManagerViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetPaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentGetAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPartBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.LoginRequest
import kotlinx.coroutines.CoroutineScope
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun AppNavHost(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    onLoginSuccess: () -> Unit,
    onSignupSuccess: () -> Unit,
    onLogout: () -> Unit,
    context: Context,
    sharedPreferences: SharedPreferences,
    drawerState: DrawerState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
    ) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Column { if (currentRoute == "main_screen") {
            TopBar(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }

        NavHost(
            navController = navController,
            startDestination  = "main_screen"
        ) {
            composable("Profile_screen") {
                ProfileScreen(
                    navController = navController,
                )
            }

            composable("SignUp_SuccessScreen") {
                SignUpSuccessScreen(
                    navController = navController,
                )
            }

            composable("server_error_screen") {
                ServerErrorScreen(navController = navController)
            }

            composable("reset_password") {
                ResetPassword(
                    navController = navController,
                    loginRequest = LoginRequest("", ""),
                )
            }

            composable("payment_error_screen") {
                PaymentErrorScreen(navController = navController,
                )
            }

            composable("popup_credit") { backStackEntry ->
                var showPopup by remember { mutableStateOf(true) }
                val name = backStackEntry.arguments?.getString("name").orEmpty()
                val time = backStackEntry.arguments?.getString("time").orEmpty()
                val price = backStackEntry.arguments?.getString("price").orEmpty()
                val mappedBookingsJson = backStackEntry.arguments?.getString("mappedBookingsJson") ?: ""
                val selectedDate = LocalDate.parse(backStackEntry.arguments?.getString("selectedDate") ?: LocalDate.now().toString())
                val bookingId = backStackEntry.arguments?.getString("bookingId")
                val playerIds = backStackEntry.arguments?.getString("playerIds")
                val playerIdList: List<Long> = playerIds?.split(",")?.mapNotNull { it.toLongOrNull() } ?: emptyList()
                val sharedExtras: List<Long> = playerIds?.split(",")?.mapNotNull { it.toLongOrNull() } ?: emptyList()
                val privateExtras: List<Long> = playerIds?.split(",")?.mapNotNull { it.toLongOrNull() } ?: emptyList()

                if (showPopup) {
                    PopupCredit(
                        onPayClick = {
                            showPopup = false
                            navController.navigate("main_screen")
                        },
                        onCancelClick = {
                            showPopup = false
                            navController.popBackStack()
                        },
                        viewModel = hiltViewModel(),
                        navController = navController,
                        errorCreditViewModel = hiltViewModel(),
                        onTotalAmountCalculated = { amount, currency -> },
                        adjustedAmount = 0.0,
                        totalExtrasCost = 0.0,
                        showPopup = showPopup,
                        onDismiss = { showPopup = false },
                        mappedBookingsJson = mappedBookingsJson,
                        viewModel9 = hiltViewModel(),
                        findTermsViewModel = hiltViewModel(),
                        selectedDate = selectedDate,
                        selectedReservation = ReservationOption(name, time, price, mappedBookingsJson),
                        saveBookingViewModel = hiltViewModel(),
                        bookingId= bookingId,
                        playerIds = playerIdList,
                        sharedExtrass = sharedExtras,
                        privateExtrass = privateExtras,
                    )
                }
            }

            composable("signup_screen") {
                SignUpScreen(
                    navController = navController,
                    onSignupSuccess = {},
                    viewModel = hiltViewModel()
                )
            }

            composable(route = "login_screen?redirectUrl={redirectUrl}", arguments = listOf(navArgument("redirectUrl") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = ""
                    })
            ) { backStackEntry ->
                val redirectUrl = backStackEntry.arguments?.getString("redirectUrl") ?: ""
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(redirectUrl.ifEmpty { "main_screen" }) {
                            popUpTo("login_screen") { inclusive = true } } },
                    viewModel = hiltViewModel(),
                    getProfileViewModel = hiltViewModel(),
                    navController = navController,
                    loginRequest = LoginRequest("", ""),
                )
            }

            composable("reservation_options/{selectedDate}", arguments = listOf(navArgument("selectedDate") { type = NavType.StringType },
            )) { backStackEntry ->
                val selectedDate = backStackEntry.arguments?.getString("selectedDate")?.let { LocalDate.parse(it) }
                val selectedTimeSlot = backStackEntry.arguments?.getString("selectedTimeSlot")

                ReservationOptions(
                    onReservationSelected = {  },
                    key = null,
                    navController = navController,
                    selectedDate = selectedDate ?: LocalDate.now(),
                    selectedTimeSlot = selectedTimeSlot,
                    viewModel = hiltViewModel(),
                    viewModel1 = hiltViewModel(),
                    viewModel2 = hiltViewModel(),
                    bookingViewModel = hiltViewModel(),
                    paymentPayAvoirViewModel = hiltViewModel(),
                    getReservationViewModel = hiltViewModel(),
                )
            }

            composable("reservation_options") { backStackEntry ->
                val selectedDate = backStackEntry.arguments?.getString("selectedDate")?.let { LocalDate.parse(it) }
                val selectedTimeSlot = backStackEntry.arguments?.getString("selectedTimeSlot")


                ReservationOptions(
                    onReservationSelected = {  },
                    key = null,
                    navController = navController,
                    selectedDate = selectedDate ?: LocalDate.now(),
                    selectedTimeSlot = selectedTimeSlot,
                    viewModel = hiltViewModel(),
                    viewModel1 = hiltViewModel(),
                    viewModel2 = hiltViewModel(),
                    bookingViewModel = hiltViewModel(),
                    paymentPayAvoirViewModel = hiltViewModel(),
                    getReservationViewModel = hiltViewModel(),

                    )
            }

            composable("CreditCharge") {
                CreditCharge(navController = navController)
            }

            composable(route = "WebViewScreen1?formUrl={formUrl}&encodedAmount={encodedAmount}&encodedId={encodedId}", arguments = listOf(
                    navArgument("formUrl") { type = NavType.StringType },
                    navArgument("encodedAmount") { type = NavType.StringType; defaultValue = "0" },
                    navArgument("encodedId") { type = NavType.LongType; defaultValue = 0L },

                    )
            ) { backStackEntry ->
                val formUrl = backStackEntry.arguments?.getString("formUrl") ?: ""
                val encodedAmount = backStackEntry.arguments?.getString("encodedAmount")?.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val paymentGetAvoirViewModel: PaymentGetAvoirViewModel = hiltViewModel()
                val encodedId = backStackEntry.arguments?.getLong("encodedId") ?: 0L

                WebViewScreen1(
                    formUrl = formUrl,
                    navController = navController,
                    paymentGetAvoirViewModel = paymentGetAvoirViewModel,
                    amount = encodedAmount,
                    Id = encodedId,

                    )
            }

            composable(route = "WebViewScreen2?formUrl={formUrl}&orderId={orderId}&BookingId={BookingId}&privateList={privateList}&encodedPartnerPayId={encodedPartnerPayId}", arguments = listOf(
                    navArgument("formUrl") { type = NavType.StringType },
                    navArgument("orderId") { type = NavType.StringType },
                    navArgument("BookingId") { type = NavType.StringType },
                    navArgument("privateList") { type = NavType.StringType; defaultValue = "" },
                    navArgument("encodedPartnerPayId") { type = NavType.StringType },

                    )
            ) { backStackEntry ->
                val formUrl = backStackEntry.arguments?.getString("formUrl") ?: ""
                val viewmodel: PaymentPartBookingViewModel = hiltViewModel()

                WebViewScreen2(
                    navController = navController,
                    viewmodel = viewmodel,
                    formUrl = formUrl,
                    onReservationClicked = { selectedDate ->
                        val key = "someKey"
                        val activityName = "SomeActivity"
                        val cityName = "SomeCity"
                        val activityId = "1"
                        val cityId = "1"
                        val establishmentId = "1"
                        val time = "10:00"
                        val isCity = false
                        val formattedDate =
                            selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        val destination =
                            "reservation_screen/$key/$formattedDate/$activityName/$cityName/$activityId/$cityId/$establishmentId/$time/$isCity"
                        navController.navigate(destination)
                    },
                )
            }

            composable(route = "WebViewScreen?paymentUrl={paymentUrl}&numberOfPart={numberOfPart}&userIds={userIds}&sharedList={sharedList}&privateList={privateList}&bookingId={bookingId}", arguments = listOf(
                    navArgument("paymentUrl") { type = NavType.StringType },
                    navArgument("numberOfPart") { type = NavType.IntType; defaultValue = 1 },
                    navArgument("userIds") { type = NavType.StringType; defaultValue = "" },
                    navArgument("sharedList") { type = NavType.StringType; defaultValue = "" },
                    navArgument("privateList") { type = NavType.StringType; defaultValue = "" },
                    navArgument("bookingId") { type = NavType.StringType; defaultValue = "" },

                )
            ) { backStackEntry ->
                val paymentUrl = backStackEntry.arguments?.getString("paymentUrl") ?: ""
                val getPaymentViewModel: GetPaymentViewModel = hiltViewModel()
                val getManagerViewModel: GetManagerViewModel = hiltViewModel()
                val getEmailViewModel: GetEmailViewModel = hiltViewModel()
                val numberOfPart = backStackEntry.arguments?.getInt("numberOfPart") ?: 1
                val encodedUserIds = backStackEntry.arguments?.getString("userIds").orEmpty()
                val encodedSharedList = backStackEntry.arguments?.getString("sharedList").orEmpty()
                val encodedPrivateList = backStackEntry.arguments?.getString("privateList").orEmpty()
                val encodedbookingId = backStackEntry.arguments?.getString("bookingId").orEmpty()

                WebViewScreen(
                    formUrl = paymentUrl,
                    navController = navController,
                    getPaymentViewModel = getPaymentViewModel,
                    getManagerViewModel = getManagerViewModel,
                    getEmailViewModel = getEmailViewModel,
                    numberOfPart = numberOfPart,
                    userIds = encodedUserIds,
                    sharedList = encodedSharedList,
                    privateList = encodedPrivateList,
                    bookingIds = encodedbookingId,
                    onReservationClicked = { selectedDate ->
                        val key = "someKey"
                        val activityName = "SomeActivity"
                        val cityName = "SomeCity"
                        val activityId = "1"
                        val cityId = "1"
                        val establishmentId = "1"
                        val time = "10:00"
                        val isCity = false
                        val formattedDate =
                            selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                        val destination =
                            "reservation_screen/$key/$formattedDate/$activityName/$cityName/$activityId/$cityId/$establishmentId/$time/$isCity"
                        navController.navigate(destination)
                    },
                    )
            }

            composable("PaymentSuccessScreen") {
                PaymentSuccessScreen(navController = navController)
            }

            composable("main_screen") {
                MainScreen(
                    navController = navController,

                    onReservationClicked = { selectedDate ->
                    },
                )
            }

            composable("main_screen") {
                MainScreen(
                    navController = navController,
                    onReservationClicked = { selectedDate ->
                        val key = "someKey"
                        val activityName = "SomeActivity"
                        val cityName = "SomeCity"
                        val activityId = "1"
                        val cityId = "1"
                        val establishmentId = "1"
                        val time = "10:00"
                        val isCity = false
                        val formattedDate =
                            selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        val destination =
                            "reservation_screen/$key/$formattedDate/$activityName/$cityName/$activityId/$cityId/$establishmentId/$time/$isCity"
                        navController.navigate(destination)
                    },
                )
            }

            composable(route = "reservation_screen/{key}/{date}/{activityName}/{cityName}/{activityId}/{cityId}/{establishmentId}/{time}/{isCity}", arguments = listOf(
                    navArgument("key") { type = NavType.StringType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("activityName") { type = NavType.StringType },
                    navArgument("cityName") { type = NavType.StringType },
                    navArgument("activityId") { type = NavType.StringType },
                    navArgument("cityId") { type = NavType.StringType },
                    navArgument("establishmentId") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType },
                    navArgument("isCity") { type = NavType.BoolType }
                )
            ) { backStackEntry ->
                val viewModel: KeyViewModel = hiltViewModel()
                val getBookingViewModel: GetBookingViewModel = hiltViewModel()
                val paymentPayAvoirViewModel: PaymentPayAvoirViewModel = hiltViewModel()

                ReservationScreen(
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    context = context,
                    sharedPreferences = sharedPreferences,
                    onFetchSuccess = {  },
                    viewModel = viewModel,
                    getBookingViewModel = getBookingViewModel,
                    paymentPayAvoirViewModel = paymentPayAvoirViewModel,
                    getReservationViewModel = hiltViewModel(),

                )
            }

            composable("payment_section1/{name}/{time}/{price}/{mappedBookings}/{selectedDate}",
                deepLinks = listOf(navDeepLink { uriPattern = "android-app://androidx.navigation/payment_section1/{name}/{time}/{price}/{mappedBookings}/{selectedDate}" }),
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType },
                    navArgument("price") { type = NavType.StringType },
                    navArgument("mappedBookings") { type = NavType.StringType },
                    navArgument("selectedDate") { type = NavType.StringType }

                    )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name").orEmpty()
                val time = backStackEntry.arguments?.getString("time").orEmpty()
                val price = backStackEntry.arguments?.getString("price").orEmpty()
                val mappedBookingsJson = backStackEntry.arguments?.getString("mappedBookings").orEmpty()
                val type = object : TypeToken<List<GetBookingResponse>>() {}.type
                val selectedDateString = backStackEntry.arguments?.getString("selectedDate").orEmpty()
                val selectedDate: LocalDate = try {
                    LocalDate.parse(selectedDateString)
                } catch (e: Exception) {
                    LocalDate.now()
                }
                PaymentSection1(
                    selectedDate = selectedDate,
                    selectedReservation = ReservationOption(name, time, price, mappedBookingsJson),
                    onExtrasUpdate = { _, _, _ -> },
                    navController = navController,
                    bookingViewModel = hiltViewModel(),
                    selectedTimeSlot = time,
                    mappedBookingsJson = mappedBookingsJson,
                    price = price,
                    onTotalAmountCalculated = { totalAmount, currency ->
                    },
                    viewModel9 = hiltViewModel(),
                    )
            }

            composable(route = "PartnerPaymentScreen/{partnerPayId}", arguments = listOf(navArgument("partnerPayId") { type = NavType.StringType })
            ) { backStackEntry ->
                val partnerPayId = backStackEntry.arguments?.getString("partnerPayId")

                PartnerPaymentScreen(
                    navController = navController,
                    partnerPayId = partnerPayId
                )
            }

            composable("CreditPayment") {
                CreditPayment(
                    navController = navController,
                )
            }

            composable("summary_screen") {
                SummaryScreen(
                    navController = navController,
                )
            }

            composable("main_screen") {
                MainScreen(
                    navController = navController,
                    onReservationClicked = { selectedDate ->
                        val key = "someKey"
                        val activityName = "SomeActivity"
                        val cityName = "SomeCity"
                        val activityId = "1"
                        val cityId = "1"
                        val establishmentId = "1"
                        val time = "10:00"
                        val isCity = false
                        val formattedDate =
                            selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                        val destination =
                            "reservation_screen/$key/$formattedDate/$activityName/$cityName/$activityId/$cityId/$establishmentId/$time/$isCity"
                        navController.navigate(destination)
                    },

                )
            }


            composable(route = "popup_partner_credit/{partnerPayId}/{totalPrice}/{selectedExtras}", arguments = listOf(
                    navArgument("partnerPayId") { type = NavType.StringType },
                    navArgument("totalPrice") { type = NavType.StringType },
                navArgument("selectedExtras") {
                    type = NavType.StringType
                    defaultValue = ""
                }
                )
            ) { backStackEntry ->
            var showPopup by remember { mutableStateOf(true) }
                val partnerPayId = backStackEntry.arguments?.getString("partnerPayId")
                val totalPriceString = backStackEntry.arguments?.getString("totalPrice").orEmpty()
                var totalPrice = totalPriceString.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val selectedExtrasString = backStackEntry.arguments?.getString("selectedExtras").orEmpty()

                val selectedExtras = if (selectedExtrasString.isNotEmpty()) {
                    // Deserialize your selectedExtras here
                    // For example, if you're using JSON: Gson().fromJson(selectedExtrasString, type)
                    emptyList<Quadruple<String, String, Int, Long>>() // Replace with actual deserialization
                } else {
                    emptyList()
                }
                if (showPopup) {
                    PopupCreditPartner(

                        navController = navController,
                        showPopup = showPopup,
                        onDismiss = { showPopup = false },
                        viewModel4 = hiltViewModel(),
                        viewModel = hiltViewModel(),
                        partnerPayId = partnerPayId,
                        totalPrice = totalPrice,
                        selectedExtras = selectedExtras

                    )
                }
            }
        }
    }
}





