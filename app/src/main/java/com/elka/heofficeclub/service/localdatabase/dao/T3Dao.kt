package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T3Entity

@Dao
interface T3Dao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t3: T3Entity)

  @Query("SELECT * FROM t3")
  fun getDocs(): List<T3Entity>

  @Delete
  fun deleteDoc(t3: T3Entity)
}