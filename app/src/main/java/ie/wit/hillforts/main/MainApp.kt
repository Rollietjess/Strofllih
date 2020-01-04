package ie.wit.hillforts.main

import android.app.Application
import ie.wit.hillforts.models.HillfortJSONStore
import ie.wit.hillforts.models.HillfortsMemStore
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.models.HillfortsStore
import ie.wit.hillforts.models.firebase.HillfortFireStore
import ie.wit.hillforts.room.HillfortStoreRoom
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

//    val hillforts = HillfortsMemStore()
//    lateinit var hillforts: HillfortsStore
//
//    override fun onCreate() {
//        super.onCreate()
//        hillforts = HillfortJSONStore(applicationContext)
//        info("Placemark started")
//
////        super.onCreate()
////        hillforts = HillfortStoreRoom(applicationContext)
////        info("Placemark started")
//    }
//}

    lateinit var hillforts: HillfortsStore

    override fun onCreate() {
        super.onCreate()
        //placemarks = PlacemarkJSONStore(applicationContext)
        //placemarks = PlacemarkStoreRoom(applicationContext)
        hillforts = HillfortFireStore(applicationContext)
        info("hillfort started")
    }

}