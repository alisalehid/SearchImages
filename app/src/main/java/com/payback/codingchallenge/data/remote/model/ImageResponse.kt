package com.payback.codingchallenge.data.remote.model

import com.google.gson.annotations.SerializedName

data class ImageResponse (
    @SerializedName("hits")
    var hits: List<Hit>,
    @SerializedName("total")
    var total: Int,
    @SerializedName("totalHits")
    var totalHits: Int ) {
}