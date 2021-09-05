package com.dhaval.bookland.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadingModes (

	@SerializedName("text") val text : Boolean,
	@SerializedName("image") val image : Boolean
) : Serializable