package com.abdullah996.g3an.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.abdullah996.g3an.R
import kotlinx.android.synthetic.main.activity_splach.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplachActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach)

        g3an.startAnimation(AnimationUtils.loadAnimation(this,R.anim.pulse))
        GlobalScope.launch(Dispatchers.Main) {
            goToLogin()
        }
    }

    private suspend fun goToLogin() {
        delay(2000)
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}