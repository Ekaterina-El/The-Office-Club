package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T7Entity

@Dao
interface T7Dao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t7: T7Entity)

  @Query("SELECT * FROM t7")
  fun getDocs(): List<T7Entity>

  @Delete
  fun deleteDoc(t7: T7Entity)
}