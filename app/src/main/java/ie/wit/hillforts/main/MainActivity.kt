package ie.wit.hillforts.main

import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.hillforts.LoginActivity
import ie.wit.hillforts.R
import ie.wit.hillforts.activities.HillfortAdapter
import ie.wit.hillforts.activities.HillfortsActivity
import ie.wit.hillforts.activities.HillfortsListener
import ie.wit.hillforts.models.HillfortsModel
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import com.google.firebase.auth.FirebaseUser
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import ie.wit.hillforts.activities.AccountActivity


class MainActivity : AppCompatActivity(), HillfortsListener, AnkoLogger {
    var fbAuth = FirebaseAuth.getInstance()
    lateinit var app: MainApp
    val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        app = application as MainApp


        Toast.makeText(this, "" + currentFirebaseUser!!.uid, Toast.LENGTH_SHORT).show()

        fab.setOnClickListener() {
            // Handler code here.
            val intent = Intent(this@MainActivity, HillfortsActivity::class.java)
            startActivityForResult(intent,0)
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = HillfortAdapter(app.hillforts.findAll(), this)
        loadHillforts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item?.itemId) {
            R.id.action_settings -> true
            R.id.logout_app -> { signOut()
                return true}
            R.id.account -> { account()
                return true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
        val intentLogout = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intentLogout)


        info("user id logout: " + currentFirebaseUser?.uid)

        Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show()
    }

    fun account(){
        val intentAccount = Intent(this@MainActivity, AccountActivity::class.java)
        startActivity(intentAccount)

    }

    override fun onHillfortsClick(hillfort: HillfortsModel) {
        startActivityForResult(intentFor<HillfortsActivity>().putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        recyclerView.adapter?.notifyDataSetChanged()
        loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadHillforts() {
        showHillforts(app.hillforts.findSpecific())
//        showHillforts(app.hillforts.findAll())
    }

    fun showHillforts (hillforts: List<HillfortsModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}