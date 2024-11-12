package com.example.banco_sisaov

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

        //Se crea la instancia de la bd
        val mbo: MiBancoOperacional? = MiBancoOperacional.getInstance(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            if ( !hasFocus && binding.tietPsw.text.toString().length < 3 || binding.tietPsw.text.toString() == "1a" ){
                binding.tfPsw.error = getString(R.string.error_psw)
            }
            else binding.tfPsw.error = null
        }

        binding.btEnter.setOnClickListener {
            if(binding.tfUsr.error == null && binding.tfPsw.error == null){
                //En la BD
                var cliente = Cliente()
                cliente.setNif(binding.tietUsr.text.toString().uppercase())
                cliente.setClaveSeguridad(binding.tietPsw.text.toString())

                // Logueamos al cliente
                var clienteLogeado = mbo?.login(cliente)

                if(clienteLogeado != null) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("Cliente", clienteLogeado)
                    startActivity(intent)
                }else{
                    Snackbar.make(binding.root, getString(R.string.error_cliente_existencia), Snackbar.LENGTH_SHORT).show()
                }
            }
            else Snackbar.make(binding.root, getString(R.string.error_general), Snackbar.LENGTH_SHORT).show()
        }

        binding.btExit.setOnClickListener {
            finish()
        }
    }

    fun validarDNI(dni:String): Boolean {
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