package com.example.banco_sisaov

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO)

        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tietUsr.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && !validarDNI(binding.tietUsr.text.toString()) ) {
                binding.tfUsr.error = getString(R.string.error_usr)
            }
            else{
                binding.tfUsr.error = null
            }
        }

        binding.tietPsw.setOnFocusChangeListener { v, hasFocus ->
            if ( !hasFocus && binding.tietPsw.text.toString().length < 8 ){
                binding.tfPsw.error = getString(R.string.error_psw)
            }
            else {
                binding.tfPsw.error = null
            }
        }

        binding.btEnter.setOnClickListener {
            if(binding.tfUsr.error == null && binding.tfPsw.error == null){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("DNI", binding.tietUsr.text.toString().uppercase())
                startActivity(intent)
            }
            else Snackbar.make(binding.root, getString(R.string.error_general), Snackbar.LENGTH_SHORT).show()
        }

        binding.btExit.setOnClickListener {
            finish()
        }
    }

    fun validarDNI(dni:String): Boolean {
        val expresionDni = Regex("^[0-9]{8}[a-zA-Z]$")
        val letrasDNI = "TRWAGMYFPDXBNJZSQVHLCKE"

        if ( expresionDni.matches(dni) ) {
            val letraCalculada = letrasDNI[dni.substring(0, 8).toInt() % 23]
            val letraIntroducida = dni[8].uppercaseChar()

            if ( letraCalculada == letraIntroducida ) return true

            return false
        }

        return false
    }
}