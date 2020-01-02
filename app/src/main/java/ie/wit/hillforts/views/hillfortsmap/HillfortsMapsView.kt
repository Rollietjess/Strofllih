package ie.wit.hillforts.views.hillfortsmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import ie.wit.hillforts.R
import ie.wit.hillforts.helpers.readImageFromPath
import ie.wit.hillforts.models.HillfortsModel
import kotlinx.android.synthetic.main.activity_hillforts_maps.*
import kotlinx.android.synthetic.main.content_hillforts_maps.*

class HillfortsMapsView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: HillfortsMapsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_maps)
        setSupportActionBar(toolbar)
        presenter = HillfortsMapsPresenter(this)

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            presenter.doPopulateMap(it)
        }
    }

    fun showHillfort(hillfort: HillfortsModel) {
        currentTitle.text = hillfort.title
        currentDescription.text = hillfort.description
        currentImage.setImageBitmap(readImageFromPath(this, hillfort.image))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}