package com.dhaval.bookland.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pdf (

	@SerializedName("isAvailable") val isAvailable : Boolean
) : Serializable