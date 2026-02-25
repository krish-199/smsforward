package com.pierreduchemin.smsforward.data.source.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ReplacementRule")
data class ReplacementRule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pattern: String,
    val replacement: String
)
