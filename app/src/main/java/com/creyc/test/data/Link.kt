package com.creyc.test.data

import com.google.gson.annotations.SerializedName

data class Link(
    @SerializedName("url") var url: String
)