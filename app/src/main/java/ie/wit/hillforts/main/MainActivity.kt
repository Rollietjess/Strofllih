package ie.wit.hillforts.main

import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import ie.wit.hillforts.LoginActivity
import ie.wit.hillforts.R
import ie.wit.hillforts.activities.HillfortsActivity
import ie.wit.hillforts.models.PlacemarkModel


class MainActivity : AppCompatActivity() {
    var fbAuth = FirebaseAuth.getInstance()
    val placemarks = ArrayList<PlacemarkModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        Toast.makeText(this, "" + currentFirebaseUser!!.uid, Toast.LENGTH_SHORT).show()

        fab.setOnClickListener() {
            // Handler code here.

            val intent = Intent(this@MainActivity, HillfortsActivity::class.java)
            startActivity(intent);
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.logout_app -> { signOut()
                return true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun signOut(){
        fbAuth.signOut()
        startActivity(
            Intent(this@MainActivity,
                LoginActivity::class.java)
        )
    }
}
