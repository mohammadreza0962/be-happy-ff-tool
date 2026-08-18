package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.FavoriteItem

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "SENSITIVITY", "HUD", "DEVICE"
    val deviceName: String,
    val ram: String,
    val summary: String,
    val payloadJson: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toFavoriteItem(): FavoriteItem = FavoriteItem(
        id = id,
        name = name,
        category = category,
        deviceName = deviceName,
        ram = ram,
        summary = summary,
        payloadJson = payloadJson,
        timestamp = timestamp
    )

    companion object {
        fun fromFavoriteItem(item: FavoriteItem): FavoriteEntity = FavoriteEntity(
            id = item.id,
            name = item.name,
            category = item.category,
            deviceName = item.deviceName,
            ram = item.ram,
            summary = item.summary,
            payloadJson = item.payloadJson,
            timestamp = item.timestamp
        )
    }
}
