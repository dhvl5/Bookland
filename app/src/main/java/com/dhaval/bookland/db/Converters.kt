package com.dhaval.bookland.db

import androidx.room.TypeConverter
import com.dhaval.bookland.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun toStatus(value: String) = enumValueOf<Status>(value)
    @TypeConverter
    fun fromStatus(value: Status) = value.name

    @TypeConverter
    fun fromVolumeInfo(volumeInfo: VolumeInfo?): String? {
        val type = object : TypeToken<VolumeInfo>() {}.type
        return Gson().toJson(volumeInfo, type)
    }
    @TypeConverter
    fun toVolumeInfo(volumeInfoString: String?): VolumeInfo? {
        val type = object : TypeToken<VolumeInfo>() {}.type
        return Gson().fromJson<VolumeInfo>(volumeInfoString, type)
    }

    @TypeConverter
    fun fromSaleInfo(saleInfo: SaleInfo?): String? {
        val type = object : TypeToken<SaleInfo>() {}.type
        return Gson().toJson(saleInfo, type)
    }
    @TypeConverter
    fun toSaleInfo(saleInfoString: String?): SaleInfo? {
        val type = object : TypeToken<SaleInfo>() {}.type
        return Gson().fromJson<SaleInfo>(saleInfoString, type)
    }

    @TypeConverter
    fun fromAccessInfo(accessInfo: AccessInfo?): String? {
        val type = object : TypeToken<AccessInfo>() {}.type
        return Gson().toJson(accessInfo, type)
    }
    @TypeConverter
    fun toAccessInfo(accessInfoString: String?): AccessInfo? {
        val type = object : TypeToken<AccessInfo>() {}.type
        return Gson().fromJson<AccessInfo>(accessInfoString, type)
    }

    @TypeConverter
    fun fromSearchInfo(searchInfo: SearchInfo?): String? {
        val type = object : TypeToken<SearchInfo>() {}.type
        return Gson().toJson(searchInfo, type)
    }
    @TypeConverter
    fun toSearchInfo(searchInfoString: String?): SearchInfo? {
        val type = object : TypeToken<SearchInfo>() {}.type
        return Gson().fromJson<SearchInfo>(searchInfoString, type)
    }
}