package com.dhaval.bookland.models

import Offers
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SaleInfo (

    @SerializedName("country") val country : String,
    @SerializedName("saleability") val saleability : String,
    @SerializedName("isEbook") val isEbook : Boolean,
    @SerializedName("listPrice") val listPrice : ListPrice,
    @SerializedName("retailPrice") val retailPrice : RetailPrice,
    @SerializedName("buyLink") val buyLink : String,
    @SerializedName("offers") val offers : List<Offers>
) : Serializable