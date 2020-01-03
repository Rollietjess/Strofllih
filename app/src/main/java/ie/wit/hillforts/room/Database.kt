package ie.wit.hillforts.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ie.wit.hillforts.models.HillfortsModel

@Database(entities = arrayOf(HillfortsModel::class), version = 1,  exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun hillfortDao(): HillfortDao
}