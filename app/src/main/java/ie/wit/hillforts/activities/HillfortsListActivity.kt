package ie.wit.hillforts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.wit.hillforts.R
import ie.wit.hillforts.main.MainActivity

class PlacemarkListActivity : AppCompatActivity() {

    lateinit var app: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        app = application as MainActivity
    }
}