package ie.wit.hillforts.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ie.wit.hillforts.R
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.HillfortsModel
import kotlinx.android.synthetic.main.activity_hillforts.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class HillfortsActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortsModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts)
        app = application as MainApp



        info("Hillforts Activity started..")

        btnAdd.setOnClickListener() {
            hillfort.title = placemarkTitle.text.toString()
            hillfort.description = description.text.toString()
            if (hillfort.title.isNotEmpty()) {
                app.hillforts.add(hillfort.copy())
                info("add Button Pressed: ${hillfort}")
                for (i in app.hillforts.indices) {
                    info("Placemark[$i]:${app.hillforts[i]}")
                }
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            } else {
                toast("Please Enter a title")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}



