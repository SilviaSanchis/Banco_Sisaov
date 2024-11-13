package com.example.banco_sisaov

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.bd.MiBancoOperacional
import com.example.banco_sisaov.databinding.ActivityContrasenaBinding
import com.example.banco_sisaov.pojo.Cliente
import com.google.android.material.snackbar.Snackbar

class ContrasenaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityContrasenaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se crea la instancia de la bd
        val mbo: MiBancoOperacional? = MiBancoOperacional.getInstance(this)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contrasena)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.contrasena.setOnClickListener {
            binding.tfPswAntigua.clearFocus()
            binding.tfPswNueva.clearFocus()
            binding.tfPswRepetida.clearFocus()

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.tfPswAntigua.windowToken, 0)

            val inputMethodManager1 = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager1.hideSoftInputFromWindow(binding.tfPswNueva.windowToken, 0)

            val inputMethodManager2 = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager2.hideSoftInputFromWindow(binding.tfPswRepetida.windowToken, 0)
        }

        //botons
        binding.btExit.setOnClickListener {
            finish()
        }

        val cliente = intent.getSerializableExtra("Cliente") as Cliente
        val pswCliente = cliente.getClaveSeguridad()

        binding.tietPswAntigua.setOnFocusChangeListener { v, hasFocus ->
            if ( !hasFocus && binding.tietPswAntigua.text.toString() != pswCliente ) {
                binding.tfPswAntigua.error = getString(R.string.error_psw_antigua)
            }
            else binding.tfPswAntigua.error = null
        }

        binding.tietPswNueva.setOnFocusChangeListener { v, hasFocus ->
            if ( !hasFocus &&  binding.tietPswNueva.text.toString().length < 3  && pswCliente == binding.tietPswNueva.text.toString() ) {
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
                && vacios()
            ) {
                cliente.setClaveSeguridad(binding.tietPswNueva.text.toString())
                val guardado = mbo?.changePassword(cliente)

                if (guardado == 1){
                    Toast.makeText(this, (R.string.contrasenya_actualizada), Toast.LENGTH_SHORT).show()
                    finish()
                }else if(guardado == 0) {
                    Snackbar.make(binding.root, getString(R.string.contrasenya_no_actualizada), Snackbar.LENGTH_SHORT).show()
                }
            }
            else Snackbar.make(binding.root, getString(R.string.error_guardar_contrasena), Snackbar.LENGTH_SHORT).show()
        }
    }

    fun vacios(): Boolean {
        return binding.tietPswAntigua.text!!.isNotEmpty() && binding.tietPswNueva.text!!.isNotEmpty() && binding.tietPswRepetida.text!!.isNotEmpty()
    }
}