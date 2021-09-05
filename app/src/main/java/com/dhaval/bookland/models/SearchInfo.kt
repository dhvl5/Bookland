package com.dhaval.bookland.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchInfo (

	@SerializedName("textSnippet") val textSnippet : String
) : Serializable