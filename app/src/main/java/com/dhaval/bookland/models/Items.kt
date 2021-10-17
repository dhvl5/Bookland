package com.dhaval.bookland.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

enum class Status {
    TO_READ, READING, FINISHED
}

@Entity(tableName = "Books")
@Parcelize
data class Items (
    var status: Status?,
    @SerializedName("kind") val kind : String,
    @PrimaryKey @SerializedName("id") val id : String,
    @SerializedName("etag") val etag : String,
    @SerializedName("selfLink") val selfLink : String,
    @SerializedName("volumeInfo") val volumeInfo : VolumeInfo,
    @SerializedName("saleInfo") val saleInfo : SaleInfo,
    @SerializedName("accessInfo") val accessInfo : AccessInfo,
    @SerializedName("searchInfo") val searchInfo : SearchInfo?,
) : Serializable, Parcelable