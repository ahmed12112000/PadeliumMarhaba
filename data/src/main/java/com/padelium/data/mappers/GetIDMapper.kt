package com.padelium.data.mappers
import com.padelium.data.dto.GetReservationIDResponseDTO
import com.padelium.data.dto.GetReservationResponseDTO
import com.padelium.domain.dto.GetReservationIDResponse
import com.padelium.domain.dto.GetReservationResponse
import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject

class GetIDMapper @Inject constructor()  {

    fun mapGetReservationResponseToGetReservationResponse(getReservationList: GetReservationIDResponseDTO): GetReservationIDResponse {
        return GetReservationIDResponse(
                id = getReservationList.id ?: 0, // Default to 0 if id is null
                from = getReservationList.from?.toString() ?: "", // Converting Instant to String or default empty string
                to = getReservationList.to?.toString() ?: "",
                annulationDate = getReservationList.annulationDate ?: Instant.now(),
                sellAmount = getReservationList.sellAmount, // BigDecimal to Double
                purchaseAmount = getReservationList.purchaseAmount,
                numberOfPlayer = getReservationList.numberOfPlayer ?: 0,
                reference = getReservationList.reference ?: "",
                description = getReservationList.description ?: "",
                isRefundable = getReservationList.isRefundable ?: false,
                created = getReservationList.created ?: "",
                updated = getReservationList.updated ?: "",
                createdBy = getReservationList.createdBy ?: 0,
                updatedBy = getReservationList.updatedBy ?: 0,
                currencyFromId = getReservationList.currencyFromId ?: 0,
                currencyToId = getReservationList.currencyToId ?: 0,
                bookingStatusId = getReservationList.bookingStatusId ?: 0,
                establishmentId = getReservationList.establishmentId ?: 0,
                userId = getReservationList.userId ?: 0,
                userLogin = getReservationList.userLogin ?: "",
                establishmentName = getReservationList.establishmentName ?: "",
                bookingStatusName = getReservationList.bookingStatusName ?: "",
                userEmail = getReservationList.userEmail ?: "",
                currencyFromSymbol = getReservationList.currencyFromSymbol ?: "",
                gainFromManager = getReservationList.gainFromManager,
                bookingStatusCode = getReservationList.bookingStatusCode ?: "",
                userPhone = getReservationList.userPhone ?: "",
                cancelBook = getReservationList.cancelBook ?: false,
                cancel = getReservationList.cancel ?: false,
                isonline = getReservationList.isonline ?: false,
                activityName = getReservationList.activityName ?: "",
                cityName = getReservationList.cityName ?: "",
                establishmentCode = getReservationList.establishmentCode ?: "",
                localAmount = getReservationList.localAmount,
                reduction = getReservationList.reduction ?: 0,
                showcancel = getReservationList.showcancel ?: false,
                showfeedBack = getReservationList.showfeedBack ?: false,
                bookingDate = getReservationList.bookingDate ?: "",
                token = getReservationList.token ?: "",
                paymentError = getReservationList.paymentError ?: false,
                paymentprog = getReservationList.paymentprog ?: false,
                amountToPay = getReservationList.amountToPay?: BigDecimal.ZERO,
                sobflousCode = getReservationList.sobflousCode ?: "",
                couponId = getReservationList.couponId ?: 0,
                isCoupon = getReservationList.isCoupon ?: false,
                couponValue = getReservationList.couponValue ?: "",
                couponCode = getReservationList.couponCode ?: "",
                establishmentPacksId = getReservationList.establishmentPacksId ?: 0,
                establishmentTypeCode = getReservationList.establishmentTypeCode ?: "",
                isConfirmed = getReservationList.isConfirmed ?: false,
                isFromEvent = getReservationList.isFromEvent ?: false,
                establishmentPacksFirstTitle = getReservationList.establishmentPacksFirstTitle ?: "",
                establishmentPacksSecondTitle = getReservationList.establishmentPacksSecondTitle ?: "",
                usersIds = getReservationList.usersIds ?: emptyList(),
                bookingUsersPaymentListDTO = getReservationList.bookingUsersPaymentListDTO ?: emptyList(),
                fromStr = getReservationList.fromStr ?: "",
                toStr = getReservationList.toStr ?: "",
                fromStrTime = getReservationList.fromStrTime ?: "",
                toStrTime = getReservationList.toStrTime ?: "",
                activePayment = getReservationList.activePayment ?: false,
                isWaitForPay = getReservationList.isWaitForPay ?: false,
                bookingLabelId = getReservationList.bookingLabelId ?: 0,
                bookingLabelName = getReservationList.bookingLabelName ?: "",
                bookingLabelColors = getReservationList.bookingLabelColors ?: "",
                sharedExtrasIds = getReservationList.sharedExtrasIds ?: emptyList(),
                privateExtrasIds = getReservationList.privateExtrasIds ?: emptyList(),
                privateExtrasLocalIds = getReservationList.privateExtrasLocalIds ?: mutableMapOf(),
                userFirstName = getReservationList.userFirstName ?: "",
                userLastName = getReservationList.userLastName ?: "",
                extras = getReservationList.extras ?: emptyList(),
                numberOfPart = getReservationList.numberOfPart ?: 0,
                createdStr = getReservationList.createdStr ?: ""
            )
        }

}
