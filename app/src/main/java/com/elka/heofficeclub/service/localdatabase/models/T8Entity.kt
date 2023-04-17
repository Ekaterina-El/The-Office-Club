package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "t8")
data class T8Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,

  @ColumnInfo(name = "date_dismissal") val dateDismissal: String,
  @ColumnInfo(name = "foundation") val foundation: String,
  @ColumnInfo(name = "foundation_doc") val foundationDoc: String,
  @ColumnInfo(name = "foundation_number") val foundationNumber: String,
  @ColumnInfo(name = "foundation_date") val foundationDate: String,
)