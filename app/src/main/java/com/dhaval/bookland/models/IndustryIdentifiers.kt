package com.dhaval.bookland.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class IndustryIdentifiers (

	@SerializedName("type") val type : String,
	@SerializedName("identifier") val identifier : String
) : Serializable