package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class QrCreateResponse(
    @field:Json(name = "qrCodeUrl")
    val qrCodeUrl: String,
)