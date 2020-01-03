package ie.wit.hillforts.views.hillfortsmap

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.views.BasePresenter
import ie.wit.hillforts.views.BaseView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HillfortsMapsPresenter(view: BaseView) : BasePresenter(view) {

    fun doPopulateMap(map: GoogleMap, hillforts: List<HillfortsModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        hillforts.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
//        val hillfort = app.hillforts.findById(tag)
//        if (hillfort != null) view?.showHillfort(hillfort)
        doAsync {
            val hillfort = app.hillforts.findById(tag)
            uiThread {
                if (hillfort != null) view?.showHillfort(hillfort)
            }
        }

    }

    fun loadHillforts() {
//        view?.showHillforts(app.hillforts.findSpecific())
        doAsync {
            val hillforts = app.hillforts.findAll()
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }
}