package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T6Entity

@Dao
interface T6Dao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t6: T6Entity)

  @Query("SELECT * FROM t6")
  fun getDocs(): List<T6Entity>

  @Delete
  fun deleteDoc(t6: T6Entity)
}