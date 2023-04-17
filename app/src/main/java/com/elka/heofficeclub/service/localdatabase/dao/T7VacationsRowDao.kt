package com.elka.heofficeclub.service.localdatabase.dao

import androidx.room.*
import com.elka.heofficeclub.service.localdatabase.models.T7RowEntity
import com.elka.heofficeclub.service.localdatabase.models.T7VacationsRowEntity

@Dao
interface T7VacationsRowDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(t7Row: T7VacationsRowEntity)

  @Query("SELECT * FROM t7_vacations_rows")
  fun getDocs(): List<T7VacationsRowEntity>

  @Delete
  fun deleteDoc(t7VacationsRow: T7VacationsRowEntity)

}