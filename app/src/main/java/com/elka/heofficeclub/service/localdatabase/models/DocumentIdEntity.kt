package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "document_id_entity")
data class DocumentIdEntity(
  @PrimaryKey var id: String,
)