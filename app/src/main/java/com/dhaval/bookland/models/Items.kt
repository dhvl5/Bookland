import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Items (

	@SerializedName("kind") val kind : String,
	@SerializedName("id") val id : String,
	@SerializedName("etag") val etag : String,
	@SerializedName("selfLink") val selfLink : String,
	@SerializedName("volumeInfo") val volumeInfo : VolumeInfo,
	@SerializedName("saleInfo") val saleInfo : SaleInfo,
	@SerializedName("accessInfo") val accessInfo : AccessInfo,
	@SerializedName("searchInfo") val searchInfo : SearchInfo
) : Serializable