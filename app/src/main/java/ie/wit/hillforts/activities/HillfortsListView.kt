package ie.wit.hillforts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.wit.hillforts.R
import ie.wit.hillforts.main.MainView

class PlacemarkListActivity : AppCompatActivity() {

    lateinit var app: MainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        app = application as MainView
    }
}