package ie.wit.hillforts.views.favourite

import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.views.BasePresenter
import ie.wit.hillforts.views.BaseView
import ie.wit.hillforts.views.VIEW
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FavouritePresenter(view: BaseView) : BasePresenter(view) {

    fun getHillforts() = app.hillforts.findFavourite()

    fun doEditHillfort(hillfort: HillfortsModel) {
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun loadFavouriteHillforts() {

        doAsync {
            val hillforts = app.hillforts.findFavourite()
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }
}