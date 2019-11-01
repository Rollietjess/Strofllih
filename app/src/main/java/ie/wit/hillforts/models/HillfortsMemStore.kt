package ie.wit.hillforts.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}
class HillfortsMemStore : HillfortsStore, AnkoLogger {
    override fun getVisitedHillforts(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTotalHillforts(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val hillforts = ArrayList<HillfortsModel>()

    override fun findAll(): List<HillfortsModel> {
        return hillforts
    }

    override fun findSpecific(): List<HillfortsModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(hillfort: HillfortsModel) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll();
    }

    override fun update(hillfort: HillfortsModel) {
        var foundHillfort: HillfortsModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.image = hillfort.image
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            logAll();
        }
    }

    override fun delete(hillfort: HillfortsModel) {
        hillforts.remove(hillfort)
    }

    fun logAll() {
        hillforts.forEach{ info("${it}") }
    }
}