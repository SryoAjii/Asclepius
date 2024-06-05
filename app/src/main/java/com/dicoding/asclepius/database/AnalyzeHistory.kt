package com.dicoding.asclepius.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class AnalyzeHistory (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var imageUri: String? = null,
    var result: String? = null,
    var date: String? = null
): Parcelable