package com.padelium.data.dto


import androidx.compose.runtime.MutableState
import org.threeten.bp.Instant
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class EstablishmentDTO(
    val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val email: String,
    val adress: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val created: Instant,
    val updated: Instant,
    val createdBy: Long,
    val updatedBy: Long,
    val cityId: Long,
    val activityId: Long,
    val jhiEntityId: Long,
    val validated: Boolean,
    val maxNumberPlayer: Int,
    val timeSpan: BigDecimal,
    val cityName: String,
    val activityName: String,
    val userId: Long,
    val userEmail: String,
    val activityCode: String,
    val detailDescription: String,
    val activityValide: Boolean,
    val phone: String,
    val establishmentFeatureDTOList: List<establishmentFeatureDTO>,
    val contacts: List<contactDTO>,
    val createdByName: String,
    val createdByEmail: String,
    val logo: String,
    val pictures: List<Unit>,
    val facadePict: Boolean,
    val facadePict1: Boolean,
    val facadePict2: Boolean,
    val facadePict3: Boolean,
    val color: String,
    val establishmentTypeId: Long,
    val establishmentTypeCode: String,
    val isEvent: Boolean = false,
    val isSpace: Boolean = false,
    val showAsGold: Boolean = false,
    val activityActive: Boolean,
    val activitySmallIcon: String,
    val activityIcon: String,
    val isClient: Boolean = true,
    val establishmentId: Long,
    val createdDate: Instant,
    val amount: Double,
    val currencySymbol: String,
    val plannings: MutableState<List<PlanningDTO>>,
    val HappyHours: List<HappyHours> = emptyList()

    ): Serializable

data class EstablishmentPacksDTO(
    val name:String,
    val firstTitle:String,
    val secondTitle:String,
    val amount: BigDecimal,
    val aamount: BigDecimal,
    val amountfeeTrans: BigDecimal,
    val description:String,
    val descriptionDetailed:String,
    val isCoupon:Boolean,
    val created: Instant,
    val updated: Instant,
    val createdBy: Long,
    val updatedBy:Long,
    val available:Int,
    val isClosed: Boolean =false,
    val establishmentPriceId: Long,
    val from: Instant,
    val to: Instant,
    val bookingsList: List<bookingDTO>,
    val currencySymbol: String,
    val marge: BigDecimal,
    val currencyId: Long,
    val wPictureUrl:String,
    val packsFeatureDTO: MutableList<packsFeatureDTO> = ArrayList(),
    val position: Int,
    val wPicture: ByteArray,
    val mPicture:ByteArray,
    val showPosition: Int,
    var id: Long
): Serializable

data class EstablishmentPictureDTO(
    val id: Long?,
    val description: String?,
    val displayOrder: Int?,
    val extension: String?,
    val height: Int?,
    val url: String,
    val created: String?,
    val updated: String?,
    val createdBy: String?,
    val updatedBy: String?,
    val establishmentId: Long?,
    val pictureTypeId: Long?,
    val facade: String?
): Serializable

data class HappyHours (
    val secondAmount: BigDecimal,
    val happyHours: List<happyHoursDTO>,
    val withSecondPrice: Boolean,
    val reductionAmount: BigDecimal,
    val reductionSecondAmount: BigDecimal,
    val payFromAvoir: Boolean,
    val reduction: Int,
    val secondReduction: Int,
    val start: String,
    val end: String,
    val amountfeeTrans: BigDecimal,
    val samountfeeTrans: BigDecimal,
    val ramountfeeTrans: BigDecimal,
    val rsamountfeeTrans: BigDecimal,
    val couponCode: String,
    val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    val establishmentPacksId: Long,
    val users: List<Long>,
    private val plannings: List<PlanningDTO> = mutableListOf()
): Serializable

data class PlanningDTO(
    val fromStr: String,
    val toStr: String,
    val from: Instant = Instant.now(), // Default value
    val to: Instant = Instant.now(), // Default value
    val available: Int = 0, // Default value
    val openTime: Instant = Instant.now(), // Default value
    val closeTime: Instant = Instant.now(), // Default value
    val bookings: List<bookingDTO> = emptyList(), // Default value
    val availableBol: Boolean = false, // Default value
    val dayWithBooking: Boolean = false, // Default value
    val price: BigDecimal = BigDecimal.ZERO, // Default value
    val feeTransaction: BigDecimal = BigDecimal.ZERO, // Default value
    val reductionPrice: BigDecimal = BigDecimal.ZERO, // Default value
    val rfeeTransaction: BigDecimal = BigDecimal.ZERO, // Default value
    val currencySymbol: String = "", // Default value
    val reductionPriceBol: Boolean = false, // Default value
    val secondPrice: Boolean = false, // Default value
    val isHappyHours: Boolean = false, // Default value
    val annulationDate: String = "" // Default value
) : Serializable

data class establishmentFeatureDTO(
    val id:Long,
    val created: Instant,
    val updated: Instant,
    val createdBy: Long,
    val updatedBy: Long,
    val description: String,
    val establishmentId: Long,
    val featureId:Long,
    val name: String,
    val icon:String,
): Serializable

data class contactDTO(
    val id: Long,
    val firstName:String,
    val lastName:String,
    val email: String,
    val phoneOffice:String,
    val mobile:String,
    val adress: String,
    val cityName: String,
    val skype:String,
    val created: Instant,
    val updated: Instant,
    val updatedBy: Long,
    val createdBy: Long,
    val description: String,
    val contactTypes:Set<contactTypeDTO>,
    val establishmentId: Long,
): Serializable

data class contactTypeDTO(
    val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val created: LocalDate,
    val updated: LocalDate,
    val createdBy: Long,
    val updatedBy: Long,
): Serializable

data class packsFeatureDTO(
    val id: Long,
    val name: String,
    val code: String,
    val icon: String,
    val description: String,
    val created: Instant,
    val updated: Instant,
    val updatedBy: Long,
    val createdBy: Long
): Serializable

data class bookingAnnulationDTO(
    val id: Long,
    val label:String,
    val cancelLimitTime:String,
    val amount: BigDecimal,
    val amountLocal: BigDecimal,
    val bookingId:Long,
    val conditionId:Long,
    val currencyId:Long,
    val currencySymbol:String,
    val forSecondAmount:Boolean,
    val forSecondAamount:Boolean,
    val formuleAmount:String,
    val marge: BigDecimal,
    val notRefundable:Boolean
) : Serializable

data class happyHoursDTO(
    val id:Long,
    val from: Instant,
    val to: Instant,
    val establishmentPriceId: Long
) : Serializable

data class bookingDTO(
    val id: Long,
    val from: Instant,
    val to: Instant,
    val annulationDate: Instant,
    val sellAmount: BigDecimal,
    val purchaseAmount: BigDecimal,
    val numberOfPlayer: Int,
    val reference:String,
    val description: String,
    val isRefundable:Boolean,
    val created: Instant,
    val updated: Instant,
    val createdBy: Long,
    val updatedBy: Long,
    val currencyFromId:Long,
    val currencyToId:Long,
    val bookingStatusId:Long,
    val establishmentId: Long,
    val userId:Long,
    val userLogin:String,
    val establishmentName:String,
    val bookingStatusCode:String,
    val userPhone:String,
    val cancelBook:Boolean,
    val cancel:Boolean,
    val isonline:Boolean,
    val activityName: String,
    val cityName: String,
    val establishmentCode:String,
    val localAmount: BigDecimal,
    val reduction: Int,
    val showcancel:Boolean,
    val showfeedBack:Boolean,
    val bookingDate:String,
    val token:String,
    val paymentError:Boolean,
    val paymentprog:Boolean,
    val amountToPay: BigDecimal,
    val sobflousCode:String,
    val couponId:Long,
    val isCoupon: Boolean,
    val couponValue:String,
    val couponCode: String,
    val establishmentPacksId: Long,
    val establishmentTypeCode: String,
    val isConfirmed:Boolean,
    val isFromEvent:Boolean,
    val establishmentPacksFirstTitle:String,
    val establishmentPacksSecondTitle:String,
    val usersIds:List<Long>,
    val bookingUsersPaymentListDTO:List<bookingUsersPaymentDTO>,
    val fromStr: String,
    val toStr: String,
    val fromStrTime:String,
    val toStrTime:String,
    val activePayment: Boolean = false,
    val isWaitForPay: Boolean = false,
    val bookingLabelId:Long,
    val bookingLabelName:String,
    val bookingLabelColors:String,
    val sharedExtrasIds: List<Long>,
    val privateExtrasIds:List<Long>,
    val userFirstName:String,
    val userLastName:String,
    val extras:List<bookingExtrasDTO>,
    val numberOfPart:Int,
    val createdStr:String,
    private val privateExtrasLocalIds: MutableMap<Long, List<Long>> = mutableMapOf()
) : Serializable

data class bookingUsersPaymentDTO(
    val id: Long,
    val code: String,
    val isactive:Boolean,
    val description: String,
    val amount: BigDecimal,
    val amountStr:String,
    val bookingId: Long,
    val bookingDate: Instant,
    val bookingDateStr:String,
    val bookingEstablishmentName:String,
    val bookingCreatedFirstName:String,
    val bookingCreatedLastName:String,
    val userId: Long,
    val userLogin: String,
    val userEmail: String,
    val currencyId: Long,
    val BookingUsersPaymentStatusId:Long,
    val userLastName:String,
    val userFirstName:String,
    val BookingUsersPaymentStatusName:String,
    val paymentMode:String,
    val paymentStatus:Boolean = true,
    val BookingUsersPaymentStatusCode:String
) : Serializable

data class bookingExtrasDTO(
    val id: Long,
    val amount: BigDecimal,
    val description: String,
    val numberOfExtra:String,
    val bookingId:Long,
    val extraId:Long,
    val userId:Long,
    val userFirstName:String,
    val userLastName:String,
    val isPrivateExtra: Boolean,
    val isSharedExtra: Boolean,
    val extraName:String
) : Serializable

data class establishmentSearchDTO(
    val dateTime: String,
    val activityId: Long,
    val cityId: Long,
    val activityName: String,
    val cityName: String,
    val establishmentId: Long,
    val key:String,
    val iscity:Boolean,
    val time:String,
    val establishmentPictureDTO: List<EstablishmentPictureDTO>,
    val amount: BigDecimal,
    val decimalNumber: Int,
    val currencySymbol: String,
    val isClient:Boolean = true,
    val facadeUrl: String,
    val openTime: Instant,
    val closeTime: Instant,
    val searchDate: LocalDate,
    val from: Instant,
    val to: Instant,
    val numberOfPlayer: Int,
    val description: String,
    val Aamount: BigDecimal,
    val currencyId: Long,
    val mgAmount: BigDecimal,
    val totalFeed: Int,
    val moyFeed: Double
) : Serializable

