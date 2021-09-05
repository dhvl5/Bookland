package com.dhaval.bookland.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageLinks (

	@SerializedName("smallThumbnail") val smallThumbnail : String,
	@SerializedName("thumbnail") val thumbnail : String
) : Serializable