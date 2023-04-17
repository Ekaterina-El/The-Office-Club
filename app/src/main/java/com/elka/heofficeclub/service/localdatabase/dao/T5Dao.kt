package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T5Entity

@Dao
interface T5Dao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t5: T5Entity)

  @Query("SELECT * FROM t5")
  fun getDocs(): List<T5Entity>

  @Delete
  fun deleteDoc(t5: T5Entity)
}