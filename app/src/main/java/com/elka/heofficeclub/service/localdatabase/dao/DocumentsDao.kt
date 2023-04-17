package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.DocumentEntity

@Dao
interface DocumentsDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(documentEntity: DocumentEntity)

  @Query("SELECT * FROM docs")
  fun getIds(): List<DocumentEntity>

  @Delete
  fun deleteId(documentEntity: DocumentEntity)
}