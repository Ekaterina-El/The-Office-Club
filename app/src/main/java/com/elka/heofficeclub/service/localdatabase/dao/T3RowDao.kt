package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T3RowEntity
import com.elka.test.models.*

@Dao
interface T3RowDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t3Row: T3RowEntity)

  @Query("SELECT * FROM t3_rows")
  fun getDocs(): List<T3RowEntity>

  @Delete
  fun deleteDoc(t3Row: T3RowEntity)

  @Query("SELECT * FROM t3_rows WHERE id LIKE :id")
  fun getDoc(id : Int) : T3RowEntity
}