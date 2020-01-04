package ie.wit.hillforts.views.hillfortlist

import ie.wit.hillforts.activities.AccountActivity
import ie.wit.hillforts.views.hillfortsmap.HillfortsMapsView
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.views.BasePresenter
import ie.wit.hillforts.views.BaseView
import ie.wit.hillforts.views.VIEW
import ie.wit.hillforts.views.hillfort.HillfortsView
import org.jetbrains.anko.*

class HillfortsListPresenter(view: BaseView) : BasePresenter(view) {

//    var app: MainApp
////
////    init {
////        app = view.application as MainApp
////    }
////
////    //fun getHillforts() = app.hillforts.findAll()
    fun getHillforts() = app.hillforts.findSpecific()
//
////    fun doAddHillfort() {
////        view.startActivityForResult<HillfortsView>(0)
////    }
////
////    fun doEditHillfort(hillfort: HillfortsModel) {
////        view.startActivityForResult(view.intentFor<HillfortsView>().putExtra("hillfort_edit", hillfort), 0)
////    }
////
////    fun doShowHillfortsMap() {
////        view.startActivity<HillfortsMapsView>()
////    }
////
    fun doShowAccount() {
        view?.startActivity<AccountActivity>()
    }

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doEditHillfort(hillfort: HillfortsModel) {
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun doShowFavourite() {
        view?.navigateTo(VIEW.FAVOURITE)
    }

    fun loadHillforts() {
//        view?.showHillforts(app.hillforts.findSpecific())
        doAsync {
            val hillforts = app.hillforts.findSpecific()
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }
}