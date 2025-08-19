package com.padelium.data.mappers

import com.padelium.data.dto.GetProfileResponseDTO
import com.padelium.domain.dto.GetProfileResponse
import javax.inject.Inject


class GetProfileMapper @Inject constructor(){
        fun GetProfileResponseDTOToGetProfileResponse(getPacksResponseList: GetProfileResponseDTO): GetProfileResponse {
                return GetProfileResponse(
                        activated = getPacksResponseList.activated ?: false,
                        authorities = getPacksResponseList.authorities ?: emptyList(),
                        avoir =getPacksResponseList.avoir ,
                        createdBy = getPacksResponseList.createdBy ?: "",
                        valcreatedDate = getPacksResponseList.valcreatedDate ?: "",
                        email = getPacksResponseList.email ?: "",
                        establishmentsIds = getPacksResponseList.establishmentsIds ?: "",
                        file = getPacksResponseList.file ?: "",
                        firstName = getPacksResponseList.firstName ?: "",
                        id = getPacksResponseList.id ,
                        image = getPacksResponseList.image ?: "",
                        imageUrl = getPacksResponseList.imageUrl ?: "",
                        isOwnerestablishmentsIds = getPacksResponseList.isOwnerestablishmentsIds ?: "",
                        langKey = getPacksResponseList.langKey ?: "",
                        lastModifiedBy = getPacksResponseList.lastModifiedBy ?: "",
                        lastModifiedDate = getPacksResponseList.lastModifiedDate ?: "",
                        lastName = getPacksResponseList.lastName ?: "",
                        login = getPacksResponseList.login ?: "",
                        phone = getPacksResponseList.phone ?: "",
                        socialMediaId = getPacksResponseList.socialMediaId ?: ""
                )
    }
}



