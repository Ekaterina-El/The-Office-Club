package com.elka.heofficeclub.service.localdatabase.dao


import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.DocumentIdEntity

@Dao
interface DocumentIdDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(documentIdEntity: DocumentIdEntity)

  @Query("SELECT * FROM document_id_entity")
  fun getIds(): List<DocumentIdEntity>

  @Delete
  fun deleteId(documentIdEntity: DocumentIdEntity)
}