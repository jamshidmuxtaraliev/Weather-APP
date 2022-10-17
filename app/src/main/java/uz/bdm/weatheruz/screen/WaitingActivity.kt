package uz.bdm.weatheruz.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import uz.bdm.weatheruz.databinding.ActivityWaitingBinding

class WaitingActivity : AppCompatActivity() {
    lateinit var binding: ActivityWaitingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            finish()
            startActivity(Intent(this, MainActivity::class.java))
            }, 3000)
    }
}