package com.payback.data.remote

import com.google.gson.annotations.SerializedName
import com.payback.data.local.Image

data class ImageResponse (
    @SerializedName("hits")
    val hits: List<Image>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalHits")
    val totalHits: Int ) {
}