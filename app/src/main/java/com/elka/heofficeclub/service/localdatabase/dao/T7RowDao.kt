package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T7RowEntity
import com.elka.test.models.*

@Dao
interface T7RowDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t7Row: T7RowEntity)

  @Query("SELECT * FROM t7_rows")
  fun getDocs(): List<T7RowEntity>

  @Delete
  fun deleteDoc(t7Row: T7RowEntity)

  @Query("SELECT * FROM t7_rows WHERE id LIKE :id")
  fun getDoc(id : Int) : T7RowEntity
}