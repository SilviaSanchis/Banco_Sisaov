package com.example.banco_sisaov

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.bd.MiBancoOperacional
import com.example.banco_sisaov.databinding.ActivityGlobalPositionBinding
import com.example.banco_sisaov.fragments.AccountsFragment
import com.example.banco_sisaov.fragments.AccountsMovementsFragment
import com.example.banco_sisaov.fragments.CuentaListener
import com.example.banco_sisaov.pojo.Cliente
import com.example.banco_sisaov.pojo.Cuenta

class GlobalPositionActivity : AppCompatActivity()/*, OnClickListener*/, CuentaListener {

    private lateinit var binding: ActivityGlobalPositionBinding

//    private lateinit var cuentaAdapter: CuentaAdapter
//    private lateinit var linearLayoutManager: LinearLayoutManager

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
//        val cuentas = mbo?.getCuentas(cliente) as ArrayList<*>
//
//        cuentaAdapter = CuentaAdapter(cuentas, this)
//        linearLayoutManager = LinearLayoutManager(this)
//
//        binding.rVGP.apply {
//            layoutManager = linearLayoutManager
//            adapter = cuentaAdapter
//        }


        val frgAccounts: AccountsFragment = AccountsFragment.newInstance(cliente)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentCuenta, frgAccounts).commit()
        frgAccounts.setCuentaListener(this)
    }

    override fun onCuentaSeleccionada(c: Cuenta) {
        if (c != null) {
            var haySegundoFC = binding.fcGPM?.let { supportFragmentManager.findFragmentById(it.id) } != null

            if (haySegundoFC) {
                //se rellena el fragment movements de la mateixa activitat

                val movementFragment = AccountsMovementsFragment.newInstance(c)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fcGPM, movementFragment)
                transaction.commitNow()
            }
            else {
                //se llanca la nova activitat digentli la cuenta
                val intent = Intent(this, GlobalPositionDetailsActivity::class.java).apply {
                    putExtra("Cuenta", c)
                }
                startActivity(intent)
            }
        }
    }
}