package ie.wit.hillforts.views.hillfortlist

import ie.wit.hillforts.activities.AccountActivity
import ie.wit.hillforts.views.hillfortsmap.HillfortsMapsView
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.views.hillfort.HillfortsView

class HillfortsListPresenter(val view: MainView) {

    var app: MainApp

    init {
        app = view.application as MainApp
    }

    //fun getHillforts() = app.hillforts.findAll()
    fun getHillforts() = app.hillforts.findSpecific()

    fun doAddHillfort() {
        view.startActivityForResult<HillfortsView>(0)
    }

    fun doEditHillfort(hillfort: HillfortsModel) {
        view.startActivityForResult(view.intentFor<HillfortsView>().putExtra("hillfort_edit", hillfort), 0)
    }

    fun doShowHillfortsMap() {
        view.startActivity<HillfortsMapsView>()
    }

    fun doShowAccount() {
        view.startActivity<AccountActivity>()
    }
}