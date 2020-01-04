package ie.wit.hillforts.views.favourite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.hillforts.R
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.views.BaseView
import ie.wit.hillforts.views.hillfortlist.HillfortAdapter
import ie.wit.hillforts.views.hillfortlist.HillfortsListener
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.*

class FavouriteView : BaseView(), HillfortsListener {
    lateinit var presenter: FavouritePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setSupportActionBar(toolbar)

        fab_add.setOnClickListener() {
            presenter.doAddHillfort()
        }

        presenter = initPresenter(FavouritePresenter(this)) as FavouritePresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadFavouriteHillforts()
    }

    override fun onHillfortsClick(hillfort: HillfortsModel) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        showHillforts(presenter.getHillforts())
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_account, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.account_back -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showHillforts (hillforts: List<HillfortsModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}