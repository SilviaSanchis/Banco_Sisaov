package com.example.banco_sisaov

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.databinding.ActivityAtmManagementBinding
import com.example.banco_sisaov.fragments.AtmButtonsFragment
import com.example.banco_sisaov.pojo.Cliente

class AtmManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAtmManagementBinding
    private lateinit var cliente: Cliente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAtmManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.atmManagement)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cliente = intent.getSerializableExtra("Cliente") as Cliente

        val frgAtmButtons: AtmButtonsFragment = AtmButtonsFragment.newInstance(cliente)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragContainerATM, frgAtmButtons)
        transaction.commit()
    }
}