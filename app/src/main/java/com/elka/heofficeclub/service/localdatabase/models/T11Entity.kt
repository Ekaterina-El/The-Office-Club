package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t11")
data class T11Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,

  @ColumnInfo(name = "reason") val reason: String,
  @ColumnInfo(name = "type") val type: String,
  @ColumnInfo(name = "foundation") val foundation: String,
  @ColumnInfo(name = "sum") val sum: Double,
)