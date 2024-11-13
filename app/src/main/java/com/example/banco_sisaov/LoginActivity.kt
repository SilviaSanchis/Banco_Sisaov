package com.example.banco_sisaov

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.bd.MiBancoOperacional
import com.example.banco_sisaov.databinding.ActivityLoginBinding
import com.example.banco_sisaov.pojo.Cliente
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se crea la instancia de la bd
        val mbo: MiBancoOperacional? = MiBancoOperacional.getInstance(this)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tietUsr.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && !validarDNI(binding.tietUsr.text.toString())) {
                binding.tfUsr.error = getString(R.string.error_usr)
            }
            else binding.tfUsr.error = null
        }

        binding.tietPsw.setOnFocusChangeListener { v, hasFocus ->
            if ( !hasFocus && binding.tietPsw.text.toString().length < 3 ){
                binding.tfPsw.error = getString(R.string.error_psw)
            }
            else binding.tfPsw.error = null
        }

        binding.login.setOnClickListener {
            binding.tfUsr.clearFocus()
            binding.tfPsw.clearFocus()
            binding.tietUsr.clearFocus()
            binding.tietPsw.clearFocus()

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.tietUsr.windowToken, 0)

            val inputMethodManager2 = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager2.hideSoftInputFromWindow(binding.tietPsw.windowToken, 0)
        }


        binding.btEnter.setOnClickListener {
            if(binding.tietUsr.error == null
                && binding.tietPsw.error == null
                && binding.tietUsr.text!!.isNotEmpty()
                && binding.tietUsr.text!!.isNotEmpty()
                ){

                val cliente = Cliente()
                cliente.setNif(binding.tietUsr.text.toString().uppercase())
                cliente.setClaveSeguridad(binding.tietPsw.text.toString())

                // Logueamos al cliente
                /*val clienteLogeado = try {
                    mbo?.login(cliente)
                } catch (e: Exception ) {
                    e.printStackTrace()
                    -1
                }*/
                val clienteLogeado = mbo?.login(cliente) ?: -1

                if(clienteLogeado == -1) {
                    Snackbar.make(binding.root, getString(R.string.error_cliente_existencia), Snackbar.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("Cliente", clienteLogeado)
                    startActivity(intent)
                }
            }
            else {
                Snackbar.make( binding.root, getString(R.string.error_general), Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btExit.setOnClickListener {
            finish()
        }
    }

    private fun validarDNI(dni:String): Boolean {
        val expresionDni = Regex("^[0-9]{8}[a-zA-Z]$")
        //val letrasDNI = "TRWAGMYFPDXBNJZSQVHLCKE"

        if ( expresionDni.matches(dni) ) {
            /*val letraCalculada = letrasDNI[dni.substring(0, 8).toInt() % 23]
            val letraIntroducida = dni[8].uppercaseChar()*/

            return true
        }

        return false
    }
}