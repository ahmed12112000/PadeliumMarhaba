package com.padelium.data.datasource.remote

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
import com.padelium.data.dto.GetStatusesResponseDTO
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.math.BigDecimal



interface PadeliumEndPoint {

    @Headers("Accept: application/json", "Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("api/authentication")
    suspend fun loginUser(@Field("username") username: String, @Field("password") password: String): Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/api/register")
    suspend fun signup(@Body request: SignupRequestDTO): Response<Void>

    @Headers("Content-Type: application/json")
    @POST("/api/account/dis/delete")
    suspend fun DeleteAccount(@Body email: RequestBody): Response<Void>

    @Headers("Content-Type: application/json")
    @POST("/api/logout")
    suspend fun logoutUser(@Body request: logoutRequestDTO): Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/api/establishments/search/init")
    suspend fun getReservationKey(@Body request: FetchKeyRequestDTO): Response<FetchKeyResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/establishments/search/list")
    suspend fun SearchList(@Body key: RequestBody): Response<List<SearchListResponse>>

    @Headers("Content-Type: application/json")
    @POST("/api/establishments/search/get/init")
    suspend fun GetInit(@Body key: RequestBody): Response<GetInitResponse>

    @Headers("Content-Type: application/json")
    @POST("api/establishments/search/init/booking")
    suspend fun InitBooking(@Body request: InitBookingRequest): Response<List<InitBookingResponse>>

    @Headers("Content-Type: application/json")
    @POST("api/establishments/search/get/booking")
    suspend fun GetBooking(@Body key: RequestBody): Response<List<GetBookingResponse>>

    @Multipart
    @POST("/api/account")
    suspend fun Profile(@Part("account")  accountJson: RequestBody, @Part file: MultipartBody.Part?): Response<Void>

    @Headers("Content-Type: application/json")
    @POST("api/account/from/social/media/all")
    suspend fun GetPassword(@Body email: RequestBody): Response<Boolean>

    @Headers("Content-Type: application/json")
    @POST("api/account/reset-password/init")
    suspend fun ResetPassword(@Body email: RequestBody): Response<Void>

    @Headers("Accept: application/json" )
    @GET("api/packs/online")
    suspend fun GetPacks(): Response<List<GetPacksResponseDTO>>

    @Headers("Accept: application/json" )
    @GET("api/user-avoirs")
    suspend fun GetCreditPay(): Response<List<CreditPayResponseDTO>>

    @Headers("Accept: application/json" )
    @GET("api/bookings")
    suspend fun GetReservation(): Response<List<GetReservationResponseDTO>>

    @Headers("Accept: application/json")
    @GET("/api/bookings/{id}")
    suspend fun GetProfileById(@Path("id") id: Long): Response<GetReservationIDResponseDTO>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/booking-users-payments/by/booking")
    suspend fun PartnerPay(@Body Id: Long): Response<PartnerPayResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/payment/create/part/booking")
    suspend fun PaymentPart(@Body paymentRequest: PaymentRequestDTO): Response<PaymentResponse?>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/payment/part/booking")
    suspend fun PaymentPartBooking(@Body paymentRequest: PaymentPartBookingRequestDTO): Response<Boolean>

    @Headers("Accept: application/json" )
    @GET("api/booking-statuses")
    suspend fun GetStatuses(): Response<List<GetStatusesResponseDTO>>

    @Headers("Accept: application/json")
    @GET("/api/account")
    suspend fun GetProfile(): Response<GetProfileResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/extras")
    suspend fun Extras(): Response<List<ExtrasResponseDTO>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/extras/shared")
    suspend fun SharedExtras(): Response<List<SharedExtrasResponseDTO>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/extras/private")
    suspend fun PrivateExtras(): Response<List<PrivateExtrasResponseDTO>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/establishments/search/save/booking")
    suspend fun SaveBooking(@Body saveBookingRequest: List<GetBookingResponse>): Response<List<SaveBookingResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/payment")
    suspend fun Payment(@Body paymentRequest: PaymentRequestDTO): Response<PaymentResponse?>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user-avoirs/pay/from/avoir")
    suspend fun PaymentPayAvoir(@Body amount: BigDecimal): Response<Boolean>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user-avoirs/part/booking")
    suspend fun PaymentParCredit(@Body paymentRequest: PaymentParCreditRequestDTO): Response<Boolean>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user-avoirs/balance/userId")
    suspend fun Balance(@Body Id: Long): Response<BigDecimal>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/payment/user/avoir")
    suspend fun PaymentAvoir(@Body userAvoirRequest: UserAvoirRequestDTO): Response<UserAvoirResponseDTO>

    @Headers("Content-Type: application/json")
    @POST("api/payment/get")
    suspend fun GetPayment(@Body getPaymentRequest: GetPaymentRequestDTO): Response<Boolean>

    @Headers("Content-Type: application/json")
    @POST("api/bookings/confirmation/email/to/manager")
    suspend fun GetManager(@Body bookingIds:List<Long> ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("api/bookings/confirmation/email")
    suspend fun GetEmail(@Body bookingIds:List<Long>): Response<Long>

    @Headers("Content-Type: application/json")
    @POST("api/establishments/search/confirm/booking")
    suspend fun ConfirmBooking (@Body confirmBookingRequest: ConfirmBookingRequestDTO): Response<Boolean>

    @Headers("Content-Type: application/json")
    @POST("api/payment/get/user/avoir")
    suspend fun PaymentGetAvoir (@Body paymentGetAvoirRequest: PaymentGetAvoirRequestDTO): Response<Boolean>

    @Headers("Content-Type: application/json","Content-Type: text/plain")
    @POST("api/users/by/term")
    suspend fun FindTerms (@Body term:RequestBody): Response<List<FindTermsResponse>>

    @Headers("Content-Type: application/json","Content-Type: text/plain")
    @POST("api/account/withPhone")
    suspend fun UpdatePhone (@Body Phone:RequestBody): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("api/establishments/search/error/payement/booking")
    suspend fun ErrorCredit (@Body creditErrorRequestDTO: CreditErrorRequestDTO): Response<Void>
}

