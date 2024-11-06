package com.example.banco_sisaov

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.databinding.ActivityContrasenaBinding
import com.google.android.material.snackbar.Snackbar

class ContrasenaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityContrasenaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO)

        enableEdgeToEdge()

        binding = ActivityContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contrasena)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //botons
        binding.btExit.setOnClickListener {
            finish()
        }


        val pswAntigua = intent.getStringExtra("Psw")
        binding.tietPswAntigua.setOnFocusChangeListener { v, hasFocus ->
            if ( !hasFocus && ( binding.tietPswAntigua.text.toString() != pswAntigua ) ) {
                binding.tfPswAntigua.error = getString(R.string.error_psw_antigua)
            }
            else binding.tietPswAntigua.error = null
        }

        binding.tietPswNueva.setOnFocusChangeListener { v, hasFocus ->
            if ( !hasFocus && ( binding.tietPswNueva.text.toString().length < 8 ) ) {
                binding.tfPswNueva.error = getString(R.string.error_psw)
            }
            else binding.tfPswNueva.error = null
        }

        binding.tietPswRepetida.setOnFocusChangeListener { v, hasFocus ->
            if ( !hasFocus && ( binding.tietPswRepetida.text.toString() != binding.tietPswNueva.text.toString() ) ) {
                binding.tfPswRepetida.error = getString(R.string.error_psw_repetida)
            }
            else binding.tfPswRepetida.error = null
        }

        binding.btGuardar.setOnClickListener {
            //que no ixca si no esta tot correcte
            if ( binding.tietPswAntigua.error ==  null
                && binding.tietPswNueva.error == null
                && binding.tietPswRepetida.error == null
                && !binding.tietPswAntigua.text!!.isEmpty()
                && !binding.tietPswNueva.text!!.isEmpty()
                && !binding.tietPswRepetida.text!!.isEmpty()) finish()
            else Snackbar.make(binding.root, getString(R.string.error_guardar_contrasena), Snackbar.LENGTH_SHORT).show()
        }
    }
}