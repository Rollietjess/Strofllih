package ie.wit.hillforts.views.hillfort

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
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import ie.wit.hillforts.R
import ie.wit.hillforts.helpers.readImageFromPath
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.models.Location
import ie.wit.hillforts.views.BaseView
import kotlinx.android.synthetic.main.activity_hillforts.*
import kotlinx.android.synthetic.main.activity_hillforts.description
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

var button_date: Button? = null
var textview_date: TextView? = null

var cal = Calendar.getInstance()

class HillfortsView : BaseView(), AnkoLogger {
    lateinit var presenter: HillfortsPresenter
    var hillfort = HillfortsModel()
    lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts)

        textview_date = this.text_view_date_1
        button_date = this.button_date_1

        text_view_date_1.isVisible = false
        button_date_1.isVisible = false

        textview_date!!.text = "--/--/----"


        presenter = HillfortsPresenter(this)

        mapViewLocation.onCreate(savedInstanceState);
        mapViewLocation.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
        }


        fab_save.setOnClickListener {
//            info("star: " + toggleButtonDrawable.isChecked)
            if (placemarkTitle.text.toString().isEmpty()) {
                toast(R.string.enter_hillfort_title)
            } else {
                presenter.doAddOrSave(
                    placemarkTitle.text.toString(),
                    description.text.toString(),
                    checkbox_visited.isChecked,
                    text_view_date_1.text.toString(),
                    additionalNotes.text.toString(),
                    ratingBar.rating,
                    toggleButtonDrawable.isChecked
                )
            }
        }

        checkbox_visited.setOnClickListener() {
            text_view_date_1.isVisible = false
            button_date_1.isVisible = false
            if (checkbox_visited.isChecked) {
                text_view_date_1.isVisible = true
                button_date_1.isVisible = true
            }
        }


        chooseImage.setOnClickListener { presenter.doSelectImage() }

//        hillfortLocation.setOnClickListener { presenter.doSetLocation() }
        mapViewLocation.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@HillfortsView,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }

    override fun showHillfort(hillfort: HillfortsModel) {
        placemarkTitle.setText(hillfort.title)
        description.setText(hillfort.description)
        checkbox_visited.isChecked = hillfort.visited
        additionalNotes.setText(hillfort.additionalNotes)
        ratingBar.rating = hillfort.rating
        toggleButtonDrawable.isChecked = hillfort.favourite
//        hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
        Glide.with(this).load(hillfort.image).into(hillfortImage);
//        lat.setText("Lat: %.6f".format(hillfort.lat))
//        lng.setText("Lng: %.6f".format(hillfort.lng))
        this.showLocation(hillfort.location)
    }

    override fun showLocation(location: Location) {
        lat.setText("%.6f".format(location.lat))
        lng.setText("%.6f".format(location.lng))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        if (presenter.edit) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
                toast(R.string.toast_delete)
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.item_delete).isVisible = false
        if (intent.hasExtra("hillfort_edit")) {
            invalidateOptionsMenu()
            menu.findItem(R.id.item_delete).isVisible = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewLocation.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapViewLocation.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapViewLocation.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapViewLocation.onResume()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapViewLocation.onSaveInstanceState(outState)
    }
}
