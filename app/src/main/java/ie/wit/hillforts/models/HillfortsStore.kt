package ie.wit.hillforts.models

interface HillfortsStore {
    fun findAll(): List<HillfortsModel>
    fun create(hillfort: HillfortsModel)
    fun update(hillfort: HillfortsModel)
}