package ie.wit.hillforts.views.hillfort

import android.content.Intent
import androidx.core.view.isVisible
import org.jetbrains.anko.intentFor
import ie.wit.hillforts.helpers.showImagePicker
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.Location
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.views.*
import ie.wit.hillforts.views.editlocation.EditLoctionView
import kotlinx.android.synthetic.main.activity_hillforts.*

class HillfortsPresenter(view: BaseView) : BasePresenter(view) {

//    val IMAGE_REQUEST = 1
//    val LOCATION_REQUEST = 2

    var hillfort = HillfortsModel()
//    var location = Location(52.245696, -7.139102, 15f)
    var defaultLocation = Location(52.245696, -7.139102, 15f)
//    var app: MainApp
    var edit = false;

    init {
//        app = view.application as MainApp
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortsModel>("hillfort_edit")!!
            if(hillfort.visited){
                view.text_view_date_1.isVisible = true
                view.button_date_1.isVisible = true
                view.text_view_date_1.setText(hillfort.dateVisited)
            }
            view.showHillfort(hillfort)
        }
    }

    fun doAddOrSave(title: String, description: String, visited: Boolean, text_date: String, additionalNote: String) {
        hillfort.title = title
        hillfort.description = description
        hillfort.visited = visited

        if(visited){
            hillfort.dateVisited = text_date
        }

        hillfort.additionalNotes = additionalNote

        if (edit) {
            app.hillforts.update(hillfort)
        } else {
            app.hillforts.create(hillfort)
        }
        view?.finish()
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        app.hillforts.delete(hillfort)
        view?.finish()
    }


    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

//    fun doSetLocation() {
//        if (hillfort.zoom != 0f) {
//            location.lat = hillfort.lat
//            location.lng = hillfort.lng
//            location.zoom = hillfort.zoom
//        }
//        view.startActivityForResult(view.intentFor<EditLoctionView>().putExtra("location", location), LOCATION_REQUEST)
//    }

    fun doSetLocation() {
        if (edit == false) {
            view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", defaultLocation)
        } else {
            view?.navigateTo(
                VIEW.LOCATION,
                LOCATION_REQUEST,
                "location",
                Location(hillfort.lat, hillfort.lng, hillfort.zoom)
            )
        }
    }

//    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
////        when (requestCode) {
////            IMAGE_REQUEST -> {
////                hillfort.image = data.data.toString()
////                view.showHillfort(hillfort)
////            }
////            LOCATION_REQUEST -> {
////                location = data.extras?.getParcelable<Location>("location")!!
////                hillfort.lat = location.lat
////                hillfort.lng = location.lng
////                hillfort.zoom = location.zoom
////            }
////        }
////    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                hillfort.image = data.data.toString()
                view?.showHillfort(hillfort)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                hillfort.lat = location.lat
                hillfort.lng = location.lng
                hillfort.zoom = location.zoom
            }
        }
    }
}