package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T1Entity

@Dao
interface T1Dao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t1: T1Entity)

  @Query("SELECT * FROM t1")
  fun getDocs(): List<T1Entity>

  @Delete
  fun deleteDoc(t1: T1Entity)
}