package ie.wit.hillforts

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.view.View
import android.widget.*
import ie.wit.hillforts.views.hillfortlist.MainView
import android.widget.Toast
import ie.wit.hillforts.main.MainApp
import ie.wit.hillforts.models.firebase.HillfortFireStore
import kotlinx.android.synthetic.main.activity_login.*

lateinit var app: MainApp
class LoginView : AppCompatActivity() {
    private val TAG = "LoginView"
    //global variables
    private var email: String? = null
    private var password: String? = null
    //UI elements
    private var tvForgotPassword: TextView? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnLogin: Button? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressBar? = null
    //Firebase references
    private var mAuth: FirebaseAuth? = null
    var fireStore: HillfortFireStore? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp

        progressBar.visibility = View.GONE
        var mAuth = FirebaseAuth.getInstance()
        if (mAuth.getCurrentUser() != null) {
            if (app.hillforts is HillfortFireStore) {
                fireStore = app.hillforts as HillfortFireStore
            }
            Toast.makeText(
                this@LoginView, "Already Logged In",
                Toast.LENGTH_LONG
            ).show()
            if (fireStore != null) {
                fireStore!!.fetcHillforts {
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(this@LoginView, MainView::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }


        } else {
            initialise()
        }

    }
    private fun initialise() {
//        tvForgotPassword = findViewById<View>(R.id.tv_forgot_password) as TextView
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnCreateAccount = findViewById<View>(R.id.btn_register_account) as Button
        mProgressBar = ProgressBar(this)
        mAuth = FirebaseAuth.getInstance()
//        tvForgotPassword!!
//            .setOnClickListener { startActivity(
//                Intent(this@LoginView,
//                ForgotPasswordActivity::class.java)
//            ) }
        btnCreateAccount!!
            .setOnClickListener { startActivity(Intent(this@LoginView,
                CreateAccountView::class.java)) }
        btnLogin!!.setOnClickListener { loginUser() }

        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }
    }

    private fun loginUser() {
        showProgress()
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
//            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.setVisibility(View.VISIBLE)
            Log.d(TAG, "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    mProgressBar!!.setVisibility(View.GONE)
                    if (task.isSuccessful) {
                        // Sign in success, update UI with signed-in user's information
                        if (fireStore != null) {
                            fireStore!!.fetcHillforts {
                                Log.d(TAG, "signInWithEmail:success")
                                updateUI()
                                hideProgress()
                            }
                        }
                        else {
                            Log.d(TAG, "signInWithEmail:failed")
                            updateUI()
                            hideProgress()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginView, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val intent = Intent(this@LoginView, MainView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}
