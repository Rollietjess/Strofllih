package ie.wit.hillforts.views

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger

import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.models.Location
import ie.wit.hillforts.views.editlocation.EditLocationView
import ie.wit.hillforts.views.favourite.FavouriteView
import ie.wit.hillforts.views.hillfortsmap.HillfortsMapsView
import ie.wit.hillforts.views.hillfort.HillfortsView
import ie.wit.hillforts.views.hillfortlist.MainView

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION, HILLFORT, MAPS, LIST, FAVOURITE
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, MainView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.HILLFORT -> intent = Intent(this, HillfortsView::class.java)
            VIEW.MAPS -> intent = Intent(this, HillfortsMapsView::class.java)
            VIEW.LIST -> intent = Intent(this, MainView::class.java)
            VIEW.FAVOURITE -> intent = Intent(this, FavouriteView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar) {
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showLocation(location : Location) {}
    open fun showHillfort(hillfort: HillfortsModel) {}
    open fun showHillforts(hillforts: List<HillfortsModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}