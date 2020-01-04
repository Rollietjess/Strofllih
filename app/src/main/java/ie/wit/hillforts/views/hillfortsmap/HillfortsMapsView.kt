package ie.wit.hillforts.views.hillfortsmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import ie.wit.hillforts.R
import ie.wit.hillforts.helpers.readImageFromPath
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.views.BaseView
import kotlinx.android.synthetic.main.activity_hillforts_maps.*
import kotlinx.android.synthetic.main.content_hillforts_maps.*

class HillfortsMapsView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: HillfortsMapsPresenter
    lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_maps)
        super.init(toolbar)

        presenter = initPresenter (HillfortsMapsPresenter(this)) as HillfortsMapsPresenter

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadHillforts()
        }
    }

    override fun showHillfort(hillfort: HillfortsModel) {
        currentTitle.text = hillfort.title
        currentDescription.text = hillfort.description
//        currentImage.setImageBitmap(readImageFromPath(this, hillfort.image))
        Glide.with(this).load(hillfort.image).into(currentImage);
    }

    override fun showHillforts(hillforts: List<HillfortsModel>) {
        presenter.doPopulateMap(map, hillforts)
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