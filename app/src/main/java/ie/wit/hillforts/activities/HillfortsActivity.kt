package ie.wit.hillforts.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.navigation.NavigationView
import ie.wit.hillforts.R
import ie.wit.hillforts.helpers.readImage
import ie.wit.hillforts.helpers.readImageFromPath
import ie.wit.hillforts.helpers.showImagePicker
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.models.Location
import kotlinx.android.synthetic.main.activity_hillforts.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class HillfortsActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortsModel()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var button_date: Button? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts)
        app = application as MainApp

        info("Hillforts Activity started..")

        var edit = false
        // get the references from layout file
        textview_date = this.text_view_date_1
        button_date = this.button_date_1

        text_view_date_1.isVisible = false
        button_date_1.isVisible = false

        textview_date!!.text = "--/--/----"

        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortsModel>("hillfort_edit")!!
            placemarkTitle.setText(hillfort.title)
            description.setText(hillfort.description)
            checkbox_visited.isChecked = hillfort.visited
            additionalNotes.setText(hillfort.additionalNotes)
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))

            if (hillfort.image != "" && hillfort.image.length > 10) {
                chooseImage.setText(R.string.change_hillfort_image)
            }

            if(hillfort.visited){
                text_view_date_1.isVisible = true
                button_date_1.isVisible = true
                text_view_date_1.setText(hillfort.dateVisited)
            }
//            btnAdd.setText(R.string.save_hillfort)
        }


//        On click listener of the add hillfort button
        fab_save.setOnClickListener() {
            hillfort.title = placemarkTitle.text.toString()
            hillfort.description = description.text.toString()
            hillfort.visited = checkbox_visited.isChecked

            if(checkbox_visited.isChecked){
                hillfort.dateVisited = text_view_date_1.text.toString()
            }

            hillfort.additionalNotes = additionalNotes.text.toString()

            if (hillfort.title.isEmpty()) {
                toast(R.string.enter_hillfort_title)
            } else {
                if (edit) {
                    app.hillforts.update(hillfort.copy())
                } else {
                    app.hillforts.create(hillfort.copy())
                }

                info("add Button Pressed: $placemarkTitle")
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }

        }

        checkbox_visited.setOnClickListener() {
            text_view_date_1.isVisible = false
            button_date_1.isVisible = false
            if(checkbox_visited.isChecked){
                text_view_date_1.isVisible = true
                button_date_1.isVisible = true
            }
        }

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        hillfortLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (hillfort.zoom != 0f) {
                location.lat = hillfort.lat
                location.lng = hillfort.lng
                location.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
        }






        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@HillfortsActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        val pinMenuItem = menu?.findItem(R.id.item_delete)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                app.hillforts.delete(hillfort.copy())
                toast(R.string.toast_delete)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.image = data.getData().toString()
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_hillfort_image)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                }
            }
        }
    }
}



