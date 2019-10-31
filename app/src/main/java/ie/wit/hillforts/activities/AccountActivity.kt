package ie.wit.hillforts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.wit.hillforts.R
import kotlinx.android.synthetic.main.activity_account.*
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class AccountActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val auth = FirebaseAuth.getInstance()
        info("gebruiker: " + auth.currentUser?.email)
        username.setText("Account!!")
    }
}