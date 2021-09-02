package com.muntazif.sayibulma.view.sonuc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.databinding.ActivitySonucBinding
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.ana.AnaEkranActivity

class SonucActivity : AppCompatActivity() {

    lateinit var binding : ActivitySonucBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySonucBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homePlayer = intent.extras?.getString("homePlayer")
        val awayPlayer = intent.extras?.getString("awayPlayer")
        val homeAnswer = intent.extras?.getString("homeAnswer")
        val homeAnswer1 = intent.extras?.getString("homeAnswer1")
        val homeAnswer2 = intent.extras?.getString("homeAnswer2")
        val homeAnswer3 = intent.extras?.getString("homeAnswer3")
        val awayAnswer = intent.extras?.getString("awayAnswer")
        val awayAnswer1 = intent.extras?.getString("awayAnswer1")
        val awayAnswer2 = intent.extras?.getString("awayAnswer2")
        val awayAnswer3 = intent.extras?.getString("awayAnswer3")
        val result = intent.extras?.getString("result")

        binding.homePlayerSonucAc.text = homePlayer
        binding.awayPlayerSonucAc.text = awayPlayer
        binding.homeSayi1SonucAc.text = homeAnswer
        binding.homeSayi2SonucAc.text = homeAnswer1
        binding.homeSayi3SonucAc.text = homeAnswer2
        binding.homeSayi4SonucAc.text = homeAnswer3
        binding.awaySayi1SonucAc.text = awayAnswer
        binding.awaySayi2SonucAc.text = awayAnswer1
        binding.awaySayi3SonucAc.text = awayAnswer2
        binding.awaySayi4SonucAc.text = awayAnswer3
        binding.kazananSonucAc.text = result


        binding.anaGitSonucAc.setOnClickListener {
            val intent = Intent(this, AnaEkranActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}