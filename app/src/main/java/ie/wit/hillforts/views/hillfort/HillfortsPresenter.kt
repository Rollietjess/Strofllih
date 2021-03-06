package ie.wit.hillforts.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.hillforts.helpers.checkLocationPermissions
import ie.wit.hillforts.helpers.createDefaultLocationRequest
import ie.wit.hillforts.helpers.isPermissionGranted
import org.jetbrains.anko.intentFor
import ie.wit.hillforts.helpers.showImagePicker
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.Location
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.views.*
import ie.wit.hillforts.views.editlocation.EditLocationView
import kotlinx.android.synthetic.main.activity_hillforts.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HillfortsPresenter(view: BaseView) : BasePresenter(view) {


    var map: GoogleMap? = null
    var hillfort = HillfortsModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()

    init {
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortsModel>("hillfort_edit")!!
            if(hillfort.visited){
                view.text_view_date_1.isVisible = true
                view.button_date_1.isVisible = true
                view.text_view_date_1.setText(hillfort.dateVisited)
            }
            view.showHillfort(hillfort)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    fun doAddOrSave(title: String, description: String, visited: Boolean, text_date: String, additionalNote: String, rating: Float, favourite: Boolean) {
        hillfort.title = title
        hillfort.description = description
        hillfort.visited = visited

        if(visited){
            hillfort.dateVisited = text_date
        }

        hillfort.additionalNotes = additionalNote
        hillfort.rating = rating
        hillfort.favourite = favourite

//        if (edit) {
//            app.hillforts.update(hillfort)
//        } else {
//            app.hillforts.create(hillfort)
//        }
//        view?.finish()

        doAsync {
            if (edit) {
                app.hillforts.update(hillfort)
            } else {
                app.hillforts.create(hillfort)
            }
            uiThread {
                view?.finish()
            }
        }
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


    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.location.lat, hillfort.location.lng, hillfort.location.zoom))
    }


    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                hillfort.image = data.data.toString()
                view?.showHillfort(hillfort)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
//                hillfort.lat = location.lat
//                hillfort.lng = location.lng
//                hillfort.zoom = location.zoom
//                locationUpdate(hillfort.lat, hillfort.lng)
                hillfort.location = location
                locationUpdate(location)
            }
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hillfort.location)
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
        }
    }

    fun locationUpdate(location: Location) {
        hillfort.location = location
        hillfort.location.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.location.lat, hillfort.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.location.lat, hillfort.location.lng), hillfort.location.zoom))
//        view?.showHillfort(hillfort)
        view?.showLocation(hillfort.location)
    }


    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
}