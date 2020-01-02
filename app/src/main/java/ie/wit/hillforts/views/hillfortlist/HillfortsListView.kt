package ie.wit.hillforts.views.hillfortlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.wit.hillforts.R

class PlacemarkListActivity : AppCompatActivity() {

    lateinit var app: MainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        app = application as MainView
    }
}