package com.elka.heofficeclub.service.localdatabase.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.other.toDocFormatWithTime
import com.elka.heofficeclub.other.toDownloadDocFormat
import com.elka.heofficeclub.service.localdatabase.dao.*
import com.elka.heofficeclub.service.localdatabase.models.*
import com.elka.heofficeclub.service.model.documents.forms.*
import java.util.*

@Database(
  version = 4,
  entities = [DocumentEntity::class, T1Entity::class, T3Entity::class, T3RowEntity::class, T7RowEntity::class, T7VacationsRowEntity::class, T5Entity::class, T6Entity::class, T7Entity::class, T8Entity::class, T11Entity::class]
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun getT1Dao(): T1Dao
  abstract fun getT3Dao(): T3Dao
  abstract fun getT3RowDao(): T3RowDao
  abstract fun getT5Dao(): T5Dao
  abstract fun getT6Dao(): T6Dao
  abstract fun getT7Dao(): T7Dao
  abstract fun getT7RowDao(): T7RowDao
  abstract fun getT7VacationsRowDao(): T7VacationsRowDao
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

    val t7VacationsRowDao = getT7VacationsRowDao()
    t7VacationsRowDao.getDocs().forEach { t7VacationsRowDao.deleteDoc(it) }

    val t8Dao = getT8Dao()
    t8Dao.getDocs().forEach { t8Dao.deleteDoc(it) }

    val t11Dao = getT11Dao()
    t11Dao.getDocs().forEach { t11Dao.deleteDoc(it) }
  }

  fun getAllDocs(): List<DocForm> {
    val t1Dao = getT1Dao()
    val listT1 = t1Dao.getDocs().map { it.toDocForm() }

    val t3RowDao = getT3RowDao()
    val t3Rows = t3RowDao.getDocs()

    val t3Dao = getT3Dao()
    val listT3 = t3Dao.getDocs().map { it.toDocForm(t3Rows) }

    val t5Dao = getT5Dao()
    val listT5 = t5Dao.getDocs().map { it.toDocForm() }

    val t6Dao = getT6Dao()
    val listT6 = t6Dao.getDocs().map { it.toDocForm() }

    val t7RowDao = getT7RowDao()
    val t7Rows = t7RowDao.getDocs()

    val t7VacationsRowDao = getT7VacationsRowDao()
    val t7VacationsRows = t7VacationsRowDao.getDocs()

    val t7Dao = getT7Dao()
    val listT7 = t7Dao.getDocs().map { it.toDocForm(t7Rows, t7VacationsRows) }

    val t8Dao = getT8Dao()
    val listT8 = t8Dao.getDocs().map { it.toDocForm() }

    val t11Dao = getT11Dao()
    val listT11 = t11Dao.getDocs().map { it.toDocForm() }

    val list = mutableListOf<DocForm>()
    list.addAll(listT1)
    list.addAll(listT3)
    list.addAll(listT5)
    list.addAll(listT6)
    list.addAll(listT7)
    list.addAll(listT8)
    list.addAll(listT11)

    return list.sortedBy { it.dataCreated }.reversed()
  }

  fun addDeltaIds(delta: List<String>) {
    val docsDao = getDocumentsDao()
    delta.forEach {
      docsDao.insert(DocumentEntity(it))
    }
  }

  fun addDocs(docs: List<DocForm>) {
    val t1Dao = getT1Dao()
    val t3RowDao = getT3RowDao()
    val t3Dao = getT3Dao()
    val t5Dao = getT5Dao()
    val t6Dao = getT6Dao()
    val t7RowDao = getT7RowDao()
    val t7VacationsRowDao = getT7VacationsRowDao()
    val t7Dao = getT7Dao()
    val t8Dao = getT8Dao()
    val t11Dao = getT11Dao()


    docs.forEach {
      when (it.type) {
        FormType.T1 -> t1Dao.insert((it as T1).toT1Entity())
        FormType.T2 -> Unit
        FormType.T3 -> {
          val t3 = it as T3
          val rows = t3.rows.map { row ->
            val id = Calendar.getInstance().time.time
            t3RowDao.insert(
              T3RowEntity(
                id = id,
                division = row.division,
                position = row.position,
                countOfEmployers = row.countOfEmployees,
                salary = row.salary,
                premium = row.premium,
              )
            )
            return@map id
          }.joinToString(T3Entity.SEPARATOR)
          t3Dao.insert(it.toT3Entity(rows))
        }
        FormType.T5 -> t5Dao.insert((it as T5).toT5Entity())
        FormType.T6 -> t6Dao.insert((it as T6).toT6Entity())
        FormType.T7 -> {
          val t7 = it as T7

          val rows = t7.rows.map { row ->
            val vacations = row.vacations.map { vacation ->
              val id = Calendar.getInstance().time.time
              t7VacationsRowDao.insert(
                T7VacationsRowEntity(
                  id = id, vacation.countOfDays, vacation.dateOfStart
                )
              )
              id
            }.joinToString(T7Entity.SEPARATOR)

            val id = Calendar.getInstance().time.time
            t7RowDao.insert(
              T7RowEntity(
                id = id,
                division = row.division,
                position = row.position,
                fullName = row.fullName,
                tableNumber = row.tableNumber,
                vacations
              )
            )
            return@map id
          }.joinToString(T7Entity.SEPARATOR)
          t7Dao.insert(it.toT7Entity(rows))
        }
        FormType.T8 -> t8Dao.insert((it as T8).toT8Entity())
        FormType.T11 -> t11Dao.insert((it as T11).toT11Entity())
      }
    }
  }
}

fun T1.toT1Entity() = T1Entity(
  id = this.id,
  number = this.number,
  fileUrl = this.fileUrl ?: "",
  dataCreated = this.dataCreated.toDocFormatWithTime(),
  hiredBy = this.hiredBy?.toDocFormat() ?: "",
  hiredFrom = this.hiredFrom?.toDocFormat() ?: "",
  divisionId = this.division?.id ?: "",
  divisionName = this.division?.name ?: "",
  position = this.position?.name ?: "",
  employerName = this.fullName,
  premium = this.premium,
  trialPeriod = this.trialPeriod,
  contractData = this.contractData?.toDocFormat() ?: "",
  contractNumber = this.contractNumber,
  conditionOfWork = this.conditionOfWork,
  natureOfWork = this.natureOfWork
)

fun T3.toT3Entity(rows: String) = T3Entity(
  id = this.id,
  number = this.number,
  fileUrl = this.fileUrl ?: "",
  dataCreated = this.dataCreated.toDocFormatWithTime(),
  rows = rows
)

fun T7.toT7Entity(rows: String) = T7Entity(
  id = this.id,
  number = this.number,
  fileUrl = this.fileUrl ?: "",
  dataCreated = this.dataCreated.toDocFormatWithTime(),
  rows = rows
)

fun T5.toT5Entity() = T5Entity(
  id = this.id,
  number = this.number,
  fileUrl = this.fileUrl ?: "",
  dataCreated = this.dataCreated.toDocFormatWithTime(),
  employerName = this.employer?.T2Local?.fullName ?: "",
  reason = transferReason,
  type = typeOfChangeWork.text,
  oldDivision = oldDivision?.name ?: "",
  newDivision = newDivision?.name ?: "",
  oldPosition = oldPosition?.name ?: "",
  newPosition = newPosition?.name ?: "",
  premuim = premium,
  foundation = foundation,
  startDate = transferStart?.toDocFormat() ?: "",
  endDate = transferEnd?.toDocFormat() ?: ""
)

fun T6.toT6Entity() = T6Entity(
  id = this.id,
  number = this.number,
  fileUrl = this.fileUrl ?: "",
  dataCreated = this.dataCreated.toDocFormatWithTime(),
  employerName = this.employer?.T2Local?.fullName ?: "",
  startWork?.toDocFormat() ?: "",
  endWork?.toDocFormat() ?: "",
  startVacationA?.toDocFormat() ?: "",
  endVacationA?.toDocFormat() ?: "",
  startVacationB?.toDocFormat() ?: "",
  endVacationB?.toDocFormat() ?: "",
)

fun T8.toT8Entity() = T8Entity(
  id = this.id,
  number = this.number,
  fileUrl = this.fileUrl ?: "",
  dataCreated = this.dataCreated.toDocFormatWithTime(),
  employerName = this.employer.T2Local?.fullName ?: "",
  dateDismissal = dismissalDate?.toDocFormat() ?: "",
  foundation = reason,
  foundationDoc = reasonDoc,
  foundationNumber = reasonNumber,
  foundationDate = reasonDate?.toDocFormat() ?: ""
)

fun T11.toT11Entity() = T11Entity(
  id = this.id,
  number = this.number,
  fileUrl = this.fileUrl ?: "",
  dataCreated = this.dataCreated.toDocFormatWithTime(),
  employerName = this.employer?.T2Local?.fullName ?: "",
  reason = reason,
  type = giftType,
  foundation = description,
  sum = sum
)