package com.example.banco_sisaov

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tietUsr.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.tietUsr.text.toString().length != 9){
                binding.tfUsr.error = "Debe ser un DNI de 8 numeros y una letra"
            }
            else{
                binding.tfUsr.error = null
            }
        }

        binding.tietPsw.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.tietPsw.text.toString().length != 8){
                binding.tfPsw.error = "Debe contener almenos 8 caracteres"
            }
            else {
                binding.tfPsw.error = null
            }
        }

        binding.btEnter.setOnClickListener {
            if(binding.tfUsr.error == null && binding.tfPsw.error == null){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("DNI", binding.tietUsr.text.toString())
                startActivity(intent)
            }
            else Snackbar.make(binding.root, "Debes rellenar los campos obligatorios", Snackbar.LENGTH_SHORT).show()
        }

        binding.btExit.setOnClickListener {
            finish()
        }
    }
}