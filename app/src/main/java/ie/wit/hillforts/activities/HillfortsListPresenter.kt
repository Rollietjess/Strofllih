package ie.wit.hillforts.activities

import ie.wit.hillforts.main.MainActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.HillfortsModel

class HillfortsListPresenter(val view: MainActivity) {

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
        view.startActivity<HillfortsMapsActivity>()
    }

    fun doShowAccount() {
        view.startActivity<AccountActivity>()
    }
}