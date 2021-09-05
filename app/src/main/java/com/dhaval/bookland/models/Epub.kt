package com.dhaval.bookland.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Epub (

	@SerializedName("isAvailable") val isAvailable : Boolean,
	@SerializedName("acsTokenLink") val acsTokenLink : String
) : Serializable