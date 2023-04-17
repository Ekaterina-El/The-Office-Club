package com.elka.heofficeclub.service.localdatabase.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elka.heofficeclub.service.localdatabase.dao.*
import com.elka.heofficeclub.service.localdatabase.models.*

@Database(
  version = 2,
  entities = [DocumentEntity::class, T1Entity::class, T3Entity::class, T3RowEntity::class, T7RowEntity::class, T5Entity::class, T6Entity::class, T7Entity::class, T8Entity::class, T11Entity::class]
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun getT1Dao(): T1Dao
  abstract fun getT3Dao(): T3Dao
  abstract fun getT3RowDao(): T3RowDao
  abstract fun getT5Dao(): T5Dao
  abstract fun getT6Dao(): T6Dao
  abstract fun getT7Dao(): T7Dao
  abstract fun getT7RowDao(): T7RowDao
  abstract fun getT8Dao(): T8Dao
  abstract fun getT11Dao(): T11Dao
  abstract fun getDocumentsDao(): DocumentsDao

  fun clear() {
    val docIdDao = getDocumentsDao()
    docIdDao.getIds().forEach { docIdDao.deleteId(it) }

    val t1Dao = getT1Dao()
    t1Dao.getDocs().forEach { t1Dao.deleteDoc(it) }

    val t3Dao = getT3Dao()
    t3Dao.getDocs().forEach { t3Dao.deleteDoc(it) }

    val t3RowDao = getT3RowDao()
    t3RowDao.getDocs().forEach { t3RowDao.deleteDoc(it) }

    val t5Dao = getT5Dao()
    t5Dao.getDocs().forEach { t5Dao.deleteDoc(it) }

    val t6Dao = getT6Dao()
    t6Dao.getDocs().forEach { t6Dao.deleteDoc(it) }

    val t7Dao = getT7Dao()
    t7Dao.getDocs().forEach { t7Dao.deleteDoc(it) }

    val t7RowDao = getT7RowDao()
    t7RowDao.getDocs().forEach { t7RowDao.deleteDoc(it) }

    val t8Dao = getT8Dao()
    t8Dao.getDocs().forEach { t8Dao.deleteDoc(it) }

    val t11Dao = getT11Dao()
    t11Dao.getDocs().forEach { t11Dao.deleteDoc(it) }
  }

}