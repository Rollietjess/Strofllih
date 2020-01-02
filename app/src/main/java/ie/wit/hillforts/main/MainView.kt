package ie.wit.hillforts.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import ie.wit.hillforts.LoginView
import ie.wit.hillforts.R
import ie.wit.hillforts.activities.*
import ie.wit.hillforts.models.HillfortsModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.AnkoLogger

class MainView : AppCompatActivity(), HillfortsListener, AnkoLogger {
    lateinit var app: MainApp

    override fun onStart() {
        super.onStart()
        var mAuth = FirebaseAuth.getInstance()
        if (mAuth.getCurrentUser() == null) {
            val intentBack = Intent(this@MainView, LoginView::class.java)
            startActivity(intentBack)
        }
    }

    lateinit var presenter: HillfortsListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener() {
            // Handler code here.
//            val intent = Intent(this@MainActivityNew, HillfortsView::class.java)
//            startActivityForResult(intent,0)

            presenter.doAddHillfort()
        }

        presenter = HillfortsListPresenter(this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter =
            HillfortAdapter(presenter.getHillforts(), this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.account -> presenter.doShowAccount()
            R.id.item_map -> presenter.doShowHillfortsMap()
            R.id.logout_app -> { signOut()
                return true}
        }
        return super.onOptionsItemSelected(item)
    }

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
        val intentLogout = Intent(this@MainView, LoginView::class.java)
        startActivity(intentLogout)

        Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun onHillfortsClick(hillfort: HillfortsModel) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        showHillforts(presenter.getHillforts())
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showHillforts (hillforts: List<HillfortsModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}