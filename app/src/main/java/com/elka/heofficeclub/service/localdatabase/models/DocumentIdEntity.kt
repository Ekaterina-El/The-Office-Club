package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "docs")
data class DocumentEntity(
  @PrimaryKey val id: String,
)