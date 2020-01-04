package ie.wit.hillforts.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import ie.wit.hillforts.LoginView
import ie.wit.hillforts.R
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.views.BaseView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.AnkoLogger

class MainView :  BaseView(), HillfortsListener {
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
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)


        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_account -> {
                    presenter.doShowAccount()
                    true
                }
                R.id.navigation_all -> {
                    presenter.doShowHillfortsMap()
                    true
                }
                R.id.navigation_fave -> {
                    presenter.doShowFavourite()
                    true
                }
                R.id.navigation_logout -> {
                    signOut()
                    true
                }

                else -> {
                    true
                }
            }

        }

        presenter = initPresenter(HillfortsListPresenter(this)) as HillfortsListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadHillforts()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.account -> presenter.doShowAccount()
            R.id.item_map -> presenter.doShowHillfortsMap()
            R.id.item_favourite -> presenter.doShowFavourite()
            R.id.logout_app -> { signOut()
                return true}
        }
        return super.onOptionsItemSelected(item)
    }


    fun signOut(){
        FirebaseAuth.getInstance().signOut()
//        presenter.app.hillforts.clear()
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

    override fun showHillforts (hillforts: List<HillfortsModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}