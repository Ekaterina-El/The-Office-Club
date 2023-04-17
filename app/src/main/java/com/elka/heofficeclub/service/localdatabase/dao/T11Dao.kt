package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T11Entity
import com.elka.test.models.*

@Dao
interface T11Dao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t8: T11Entity)

  @Query("SELECT * FROM t11")
  fun getDocs(): List<T11Entity>

  @Delete
  fun deleteDoc(t11: T11Entity)
}