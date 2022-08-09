package com.example.barberapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.barberapp.view.auth.LoginActivity
import com.example.barberapp.view.dashboard.DashboardActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            val pref = getSharedPreferences("user_info", MODE_PRIVATE)
            if (pref.contains("user_Id")) {
                startActivity(Intent(this, DashboardActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000)
    }
}

/*
1. login
2. register
3. dashboard
4. navigation drawer
5. list of barber
6. booking barber
7. service
8. haircut
9. massage and spa
10. official looks
11. notification
12. rating
13. about screen
14. splash screen
15. work hours
16. Logout
* */