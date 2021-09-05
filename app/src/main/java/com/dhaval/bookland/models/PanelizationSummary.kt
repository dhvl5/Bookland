package com.dhaval.bookland.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PanelizationSummary (

	@SerializedName("containsEpubBubbles") val containsEpubBubbles : Boolean,
	@SerializedName("containsImageBubbles") val containsImageBubbles : Boolean
) : Serializable