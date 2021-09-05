package com.dhaval.bookland.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ListPrice (

	@SerializedName("amountInMicros") val amountInMicros : Long,
	@SerializedName("currencyCode") val currencyCode : String
) : Serializable