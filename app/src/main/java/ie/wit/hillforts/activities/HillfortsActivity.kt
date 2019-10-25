package ie.wit.hillforts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.wit.hillforts.R
import ie.wit.hillforts.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_hillforts.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class HillfortsActivity : AppCompatActivity(), AnkoLogger {

    var placemark = PlacemarkModel()
    val placemarks = ArrayList<PlacemarkModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts)
        info("Hillforts Activity started..")

        btnAdd.setOnClickListener() {
            placemark.title = placemarkTitle.text.toString()
            placemark.description = description.text.toString()
            if (placemark.title.isNotEmpty()) {
                placemarks.add(placemark.copy())
                info("add Button Pressed: ${placemark}")
                for (i in placemarks.indices) {
                    info("Placemark[$i]:${this.placemarks[i]}")
                }
            }
            else {
                toast ("Please Enter a title")
            }
        }
    }
}

