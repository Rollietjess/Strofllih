package ie.wit.hillforts.main

import android.app.Application
import ie.wit.hillforts.models.HillfortJSONStore
import ie.wit.hillforts.models.HillfortsMemStore
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.models.HillfortsStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

//    val hillforts = HillfortsMemStore()
    lateinit var hillforts: HillfortsStore

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortJSONStore(applicationContext)
        info("Placemark started")
//        hillforts.add(HillfortsModel("One", "About one..."))
//        hillforts.add(HillfortsModel("Two", "About two..."))
//        hillforts.add(HillfortsModel("Three", "About three..."))
    }
}