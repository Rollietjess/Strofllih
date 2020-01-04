package ie.wit.hillforts.room

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.models.HillfortsStore

class HillfortStoreRoom(val context: Context) : HillfortsStore {
    override fun getVisitedHillforts(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTotalHillforts(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var dao: HillfortDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.hillfortDao()
    }

    override fun findAll(): List<HillfortsModel> {
        return dao.findAll()
    }

    override fun findSpecific(): List<HillfortsModel> {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        return dao.findSpecific(currentFirebaseUser!!.uid)
    }

    override fun findById(id: Long): HillfortsModel? {
        return dao.findById(id)
    }

    override fun findFavourite(): List<HillfortsModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(hillfort: HillfortsModel) {
        dao.create(hillfort)
    }

    override fun update(hillfort: HillfortsModel) {
        dao.update(hillfort)
    }

    override fun delete(hillfort: HillfortsModel) {
        dao.deleteHillfort(hillfort)
    }

    override fun clear() {
    }
}