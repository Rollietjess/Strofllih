package ie.wit.hillforts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.wit.hillforts.R
import com.google.firebase.auth.FirebaseAuth
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.firebase.database.*
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.HillfortsModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

//Firebase references
private var mDatabaseReference: DatabaseReference? = null
private var mDatabase: FirebaseDatabase? = null
private var mAuth: FirebaseAuth? = null
//UI elements
private var tvFirstName: TextView? = null
private var tvEmail: TextView? = null
private var tvTotal: TextView? = null
private var tvVisited: TextView? = null

lateinit var app: MainApp



class AccountActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        app = application as MainApp

        initialise()
    }

    private fun initialise() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("users")
        mAuth = FirebaseAuth.getInstance()
        tvFirstName = findViewById<View>(R.id.tv_first_name) as TextView
        tvEmail = findViewById<View>(R.id.tv_email) as TextView
        tvTotal = findViewById<View>(R.id.tv_total_hillforts) as TextView
        tvVisited = findViewById<View>(R.id.tv_total_visited) as TextView
    }

    override fun onStart() {
        super.onStart()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)
        tvEmail!!.text = mUser.email
        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var firstname = snapshot.child("firstName").value as String
                var lastname = snapshot.child("lastName").value as String
                tvFirstName!!.text = getString(R.string.fullname, firstname, lastname);
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        tvTotal!!.text = app.hillforts.getTotalHillforts().toString()
        tvVisited!!.text = app.hillforts.getVisitedHillforts().toString()
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


}
