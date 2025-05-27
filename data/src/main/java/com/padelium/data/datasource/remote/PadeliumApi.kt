package com.padelium.data.datasource.remote

import android.content.Context
import android.net.Uri
import android.util.Log
import com.padelium.data.dto.ConfirmBookingRequestDTO
import com.padelium.data.dto.CreditErrorRequestDTO
import com.padelium.data.dto.CreditPayResponseDTO
import com.padelium.data.dto.ExtrasResponseDTO
import com.padelium.data.dto.FetchKeyRequestDTO
import com.padelium.data.dto.GetPacksResponseDTO
import com.padelium.data.dto.GetPaymentRequestDTO
import com.padelium.data.dto.GetProfileResponseDTO
import com.padelium.data.dto.GetReservationIDResponseDTO
import com.padelium.data.dto.GetReservationResponseDTO
import com.padelium.data.dto.InitBookingRequestDTO
import com.padelium.data.dto.PaymentGetAvoirRequestDTO
import com.padelium.data.dto.PaymentParCreditRequestDTO
import com.padelium.data.dto.PaymentPartBookingRequestDTO
import com.padelium.data.dto.PaymentRequestDTO
import com.padelium.data.dto.PrivateExtrasResponseDTO
import com.padelium.data.dto.SharedExtrasResponseDTO
import com.padelium.data.dto.SignupRequestDTO
import com.padelium.data.dto.UserAvoirRequestDTO
import com.padelium.data.dto.UserAvoirResponseDTO
import com.padelium.data.dto.logoutRequestDTO
import com.padelium.domain.dto.FetchKeyResponse
import com.padelium.domain.dto.FindTermsResponse
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetInitResponse
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.dto.InitBookingResponse
import com.padelium.domain.dto.PartnerPayResponse
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.dto.SearchListResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal

class PadeliumApi @Inject constructor(
    private val endPoint: PadeliumEndPoint,
    @ApplicationContext private val context: Context
) {
    suspend fun Profile(accountJson: String, imageUri: Uri?): Response<Void> {
        val accountRequestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            accountJson
        )

        val imageFile = imageUri?.let { uri ->
            uriToFile(uri, context)
        }

        val filePart = imageFile?.let {
            if (it.exists()) {
                val fileRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), it)
                MultipartBody.Part.createFormData("file", it.name, fileRequestBody)
            } else {
                null
            }
        }

        return endPoint.Profile(accountRequestBody, filePart)
    }
    private fun uriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val file = File(context.cacheDir, "temp_image")
        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }
    suspend fun loginUser(username: String, password: String): Response<ResponseBody> {
        return endPoint.loginUser( username, password)
    }
    suspend fun GetPacks(): Response<List<GetPacksResponseDTO>> {
        return endPoint.GetPacks()
    }
    suspend fun GetCreditPay(): Response<List<CreditPayResponseDTO>> {
        return endPoint.GetCreditPay()
    }
    suspend fun GetReservation(): Response<List<GetReservationResponseDTO>> {
        return endPoint.GetReservation()
    }
    suspend fun GetProfileById(id: Long): Response<GetReservationIDResponseDTO> {
        return endPoint.GetProfileById(id)
    }
    suspend fun GetProfile(): Response<GetProfileResponse> {
        return endPoint.GetProfile()
    }
    suspend fun  signup(signupRequest: SignupRequestDTO): Response<Void>{
        return endPoint.signup(signupRequest)
    }
    suspend fun  logoutUser(logoutRequest: logoutRequestDTO): Response<ResponseBody>{
        return endPoint.logoutUser(logoutRequest)
    }
    suspend fun  getReservationKey(fetchKeyRequest: FetchKeyRequestDTO): Response<FetchKeyResponse>{
        return endPoint.getReservationKey(fetchKeyRequest)
    }
    suspend fun GetInit (key: String): Response<GetInitResponse> {
        val requestBody = key.toRequestBody("application/json".toMediaTypeOrNull())
        return endPoint.GetInit(requestBody)
    }
    suspend fun SearchList (key: String): Response<List<SearchListResponse>> {
        val requestBody = key.toRequestBody("application/json".toMediaTypeOrNull())
        return endPoint.SearchList(requestBody)
    }
    suspend fun InitBooking (initBookingRequest: InitBookingRequest): Response<List<InitBookingResponse>> {
    return endPoint.InitBooking(initBookingRequest)
    }
    suspend fun GetBooking (key: String): Response<List<GetBookingResponse>> {
            val requestBody = key.toRequestBody("application/json".toMediaTypeOrNull())
            return endPoint.GetBooking(requestBody)
    }
    suspend fun GetPassword(email: String): Response<Boolean> {
        val requestBody = email.toRequestBody("application/json".toMediaTypeOrNull())
        return endPoint.GetPassword(requestBody)
    }
    suspend fun DeleteAccount(email: String): Response<Void> {
        val requestBody = email.toRequestBody("application/json".toMediaTypeOrNull())
        return endPoint.DeleteAccount(requestBody)
    }
    suspend fun ResetPassword(email: String): Response<Void> {
        val requestBody = email.toRequestBody("application/json".toMediaTypeOrNull())
        return endPoint.ResetPassword(requestBody)
    }
    suspend fun SaveBooking (saveBookingRequest: List<GetBookingResponse>): Response<List<SaveBookingResponse>> {
        return endPoint.SaveBooking(saveBookingRequest)
    }
    suspend fun Extras(): Response<List<ExtrasResponseDTO>> {
        return endPoint.Extras()
    }
    suspend fun SharedExtras(): Response<List<SharedExtrasResponseDTO>> {
        return endPoint.SharedExtras()
    }
    suspend fun PrivateExtras(): Response<List<PrivateExtrasResponseDTO>> {
        return endPoint.PrivateExtras()
    }
    suspend fun PaymentPayAvoir (amount: BigDecimal): Response<Boolean> {
        return endPoint.PaymentPayAvoir(amount)
    }
    suspend fun Balance (Id: Long): Response<BigDecimal> {
        return endPoint.Balance(Id)
    }
    suspend fun PartnerPay (Id: Long): Response<PartnerPayResponse> {
        return endPoint.PartnerPay(Id)
    }
    suspend fun PaymentPart (paymentRequest: PaymentRequestDTO): Response<PaymentResponse?> {
        return endPoint.PaymentPart(paymentRequest)
    }
    suspend fun PaymentAvoir (userAvoirRequest: UserAvoirRequestDTO):  Response<UserAvoirResponseDTO> {
        return endPoint.PaymentAvoir(userAvoirRequest)
    }
    suspend fun Payment (paymentRequest: PaymentRequestDTO): Response<PaymentResponse?> {
        return endPoint.Payment(paymentRequest)
    }
    suspend fun PaymentPartBooking (paymentGetAvoirRequest: PaymentPartBookingRequestDTO): Response<Boolean> {
        return endPoint.PaymentPartBooking(paymentGetAvoirRequest)
    }
    suspend fun PaymentParCredit (paymentParCreditRequest: PaymentParCreditRequestDTO): Response<Boolean> {
        return endPoint.PaymentParCredit(paymentParCreditRequest)
    }
    suspend fun GetPayment (getPaymentRequest: GetPaymentRequestDTO): Response<Boolean> {
        return endPoint.GetPayment(getPaymentRequest)
    }
    suspend fun GetManager (bookingIds:List<Long>): Response<Unit> {
        return endPoint.GetManager(bookingIds)
    }
    suspend fun GetEmail (bookingIds:List<Long>): Response<Long> {
        return endPoint.GetEmail(bookingIds)
    }
    suspend fun ConfirmBooking (confirmBookingRequest: ConfirmBookingRequestDTO): Response<Boolean> {
        return endPoint.ConfirmBooking(confirmBookingRequest)
    }
    suspend fun PaymentGetAvoir (paymentGetAvoirRequest: PaymentGetAvoirRequestDTO): Response<Boolean> {
        return endPoint.PaymentGetAvoir(paymentGetAvoirRequest)
    }
    suspend fun FindTerms (term:RequestBody): Response<List<FindTermsResponse>> {
        return endPoint.FindTerms(term)
    }
    suspend fun UpdatePhone (Phone:RequestBody): Response<Unit> {
        return endPoint.UpdatePhone(Phone)
    }
    suspend fun ErrorCredit (creditErrorRequestDTO: CreditErrorRequestDTO): Response<Void> {
        return endPoint.ErrorCredit(creditErrorRequestDTO)
    }
}