package ie.wit.hillforts.room

import androidx.room.*
import ie.wit.hillforts.models.HillfortsModel

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: HillfortsModel)

    @Query("SELECT * FROM HillfortsModel")
    fun findAll(): List<HillfortsModel>

    @Query("select * from HillfortsModel where id = :id")
    fun findById(id: Long): HillfortsModel

    @Query("select * from HillfortsModel where userid = :string")
    fun findSpecific(string: String): List<HillfortsModel>

    @Update
    fun update(hillfort: HillfortsModel)

    @Delete
    fun deleteHillfort(hillfort: HillfortsModel)
}