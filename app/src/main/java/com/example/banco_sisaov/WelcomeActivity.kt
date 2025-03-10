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
import com.example.banco_sisaov.bd.MiBD
import com.example.banco_sisaov.bd.MiBancoOperacional
import com.example.banco_sisaov.dao.ClienteDAO
import com.example.banco_sisaov.databinding.ActivityWelcomeBinding
import com.example.banco_sisaov.pojo.Cliente

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

        ///////////////////////////////////////CREAR USUARIO ADMIN//////////////////////////////////
        /*val cAdmin: Cliente = Cliente()
        cAdmin.setNif("00000000S")
        cAdmin.setClaveSeguridad("1111")
        cAdmin.setNombre("ADMIN")
        cAdmin.setApellidos("")
        cAdmin.setEmail("admin@admin.es")
        cAdmin.setId(20)

        val mbo: MiBancoOperacional? = MiBancoOperacional.getInstance(this)

        mbo?.addCliente(cAdmin)*/
        ////////////////////////////////////////////////////////////////////////////////////////////

        //BBDD CAJEROS
        /*Thread{
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
        }.start()*/

        Thread {
            var cajero1 =  CajeroEntity(1, "Carrer del Clariano, 1, 46021 Valencia, Valencia, España",
                39.47600769999999, -0.3524475000000393, "13f")
            CajeroApplication.dataBase.cajeroDao().updateCajero(cajero1)

            var cajero2 = CajeroEntity(2, "Avinguda del Cardenal Benlloch, 65, 46021 València, Valencia, España",
                39.4710366, -0.3547525000000178, "13f")
            CajeroApplication.dataBase.cajeroDao().updateCajero(cajero2)

            var cajero3 = CajeroEntity(3, "Av. del Port, 237, 46011 València, Valencia, España",
                39.46161999999999, -0.3376299999999901, "13f")
            CajeroApplication.dataBase.cajeroDao().updateCajero(cajero3)

            var cajero4 = CajeroEntity(4, "Carrer del Batxiller, 6, 46010 València, Valencia, España",
                39.4826729, -0.3639118999999482, "13f")
            CajeroApplication.dataBase.cajeroDao().updateCajero(cajero4)

            var cajero5 = CajeroEntity(5, "Av. del Regne de València, 2, 46005 València, Valencia, España",
                39.4647669, -0.3732760000000326, "13")
            CajeroApplication.dataBase.cajeroDao().updateCajero(cajero5)
        }
    }
}