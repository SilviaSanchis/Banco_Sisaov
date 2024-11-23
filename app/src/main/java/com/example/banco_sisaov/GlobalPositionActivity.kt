package com.example.banco_sisaov

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_sisaov.adapters.CuentaAdapter
import com.example.banco_sisaov.interfacesRecyclerViews.OnClickListenerCuenta
import com.example.banco_sisaov.bd.MiBancoOperacional
import com.example.banco_sisaov.databinding.ActivityGlobalPositionBinding
import com.example.banco_sisaov.pojo.Cliente
import com.example.banco_sisaov.pojo.Cuenta

class GlobalPositionActivity : AppCompatActivity(), OnClickListenerCuenta {

    private lateinit var binding: ActivityGlobalPositionBinding

    private lateinit var cuentaAdapter: CuentaAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Se crea la instancia de la bd
        val mbo: MiBancoOperacional? = MiBancoOperacional.getInstance(this)

        binding = ActivityGlobalPositionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.global)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cliente = intent.getSerializableExtra("Cliente") as Cliente
        val cuentas = mbo?.getCuentas(cliente) as ArrayList<*>

        cuentaAdapter = CuentaAdapter(cuentas, this)
        linearLayoutManager = LinearLayoutManager(this)

        binding.rVGP.apply {
            layoutManager = linearLayoutManager
            adapter = cuentaAdapter
        }
    }

    override fun onClick(cuenta: Cuenta) {
        Toast.makeText(this, cuenta.getNumeroCuenta(), Toast.LENGTH_SHORT).show()
    }
}