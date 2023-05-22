package com.example.nexum.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class heatmapData(
    val latitude: Double,
    val longitude: Double
)
