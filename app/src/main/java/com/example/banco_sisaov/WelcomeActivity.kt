package com.example.banco_sisaov

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.Tema9.CajeroApplication
import com.example.banco_sisaov.Tema9.CajeroEntity
import com.example.banco_sisaov.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO)

        enableEdgeToEdge()

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btWelcome.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        Thread{
            val cajerosEntityLists : List<CajeroEntity> = listOf(
                CajeroEntity(1, "Carrer del Clariano, 1, 46021 Valencia, Valencia, España",
                    39.47600769999999, -0.3524475000000393, ""),
                CajeroEntity(2, "Avinguda del Cardenal Benlloch, 65, 46021 València, Valencia, España",
                    39.4710366, -0.3547525000000178, ""),
                        CajeroEntity(3, "Av. del Port, 237, 46011 València, Valencia, España",
                    39.46161999999999, -0.3376299999999901, ""),
                CajeroEntity(4, "Carrer del Batxiller, 6, 46010 València, Valencia, España",
                    39.4826729, -0.3639118999999482, ""),
                CajeroEntity(5, "Av. del Regne de València, 2, 46005 València, Valencia, España",
                    39.4647669, -0.3732760000000326, "")
            )

            CajeroApplication.dataBase.cajeroDao().insertAll(cajerosEntityLists)
        }.start()

        Thread{
            val cajeroEntitis: List<CajeroEntity> = CajeroApplication.dataBase.cajeroDao().getAllCajeros()

            for (cajero in cajeroEntitis) {
                Log.i("CAJERO", "Cajero: ${cajero.id} ${cajero.direccion}")
            }
        }.start()
    }
}