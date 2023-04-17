package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T8Entity

@Dao
interface T8Dao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t8: T8Entity)

  @Query("SELECT * FROM t8")
  fun getDocs(): List<T8Entity>

  @Delete
  fun deleteDoc(t8: T8Entity)
}