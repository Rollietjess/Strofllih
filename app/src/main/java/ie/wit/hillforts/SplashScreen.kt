package ie.wit.hillforts

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.splash_screen.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        splash_logo.setImageResource(R.drawable.splash_screen_logo)

        //Animation for the text on the splash screen
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation)
        val textViewToBounce = findViewById<ImageView>(R.id.splash_logo)
//        val textViewToBounce = findViewById<TextView>(R.id.startName)
        textViewToBounce.startAnimation(bounceAnimation)


        val SPLASH_TIME_OUT = 2000
//        val homeIntent = Intent(this@SplashScreen, MainView::class.java)
//        val homeIntent = Intent(this@SplashScreen, CreateAccountView::class.java)
        val homeIntent = Intent(this@SplashScreen, LoginView::class.java)

        Handler().postDelayed({
            //Do some stuff here, like implement deep linking
            startActivity(homeIntent)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

}