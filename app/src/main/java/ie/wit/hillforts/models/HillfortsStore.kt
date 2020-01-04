package ie.wit.hillforts.models

interface HillfortsStore {
    fun findAll(): List<HillfortsModel>
    fun findSpecific(): List<HillfortsModel>
    fun create(hillfort: HillfortsModel)
    fun update(hillfort: HillfortsModel)
    fun delete(hillfort: HillfortsModel)
    fun getTotalHillforts(): Int
    fun getVisitedHillforts(): Int
    fun findById(id:Long) : HillfortsModel?
    fun findFavourite(): List<HillfortsModel>
    fun clear()
}